package com.chinaredstar.longyan.web.controller;

import com.chinaredstar.commonBiz.bean.RedstarCommunity;
import com.chinaredstar.commonBiz.bean.RedstarCommunityBuilding;
import com.chinaredstar.commonBiz.bean.RedstarCommunityUnit;
import com.chinaredstar.commonBiz.bean.RedstarCommunityUpdateLog;
import com.chinaredstar.commonBiz.bean.constant.CommonBizConstant;
import com.chinaredstar.commonBiz.manager.DispatchDriver;
import com.chinaredstar.commonBiz.manager.RedstarCommonManager;
import com.chinaredstar.commonBiz.util.DoubleUtil;
import com.chinaredstar.longyan.exception.BusinessException;
import com.chinaredstar.longyan.exception.constant.CommonExceptionType;
import com.chinaredstar.longyan.exception.constant.CommunityExceptionType;
import com.chinaredstar.longyan.util.RateUtil;
import com.xiwa.base.bean.PaginationDescribe;
import com.xiwa.base.bean.Response;
import com.xiwa.base.bean.SimplePaginationDescribe;
import com.xiwa.base.bean.search.ext.IntSearch;
import com.xiwa.base.bean.search.ext.MultiSearchBean;
import com.xiwa.base.pipeline.PipelineContext;
import com.xiwa.base.util.DataUtil;
import com.xiwa.base.util.StringUtil;
import com.xiwa.zeus.trinity.bean.Employee;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by StevenDong on 2016/9/21
 */
@RequestMapping("/community")
@Controller
public class CommunityController extends BaseController implements CommonBizConstant {

    @Autowired
    private DispatchDriver dispatchDriver;
    @Autowired
    private RedstarCommonManager redstarCommonManager;


    //查询我的小区列表
    @RequestMapping(value = "/myList/{type}", method = RequestMethod.POST)
    @ResponseBody
    public Response myList(@PathVariable("type") String strType) {
        PipelineContext pipelineContext = this.buildPipelineContent();
        Response res = pipelineContext.getResponse();

        try {
            // 查询参数设定
            // 登陆EmployeeID获得
            Employee loginEmployee = this.getEmployeeromSession();
            if (loginEmployee.getId() == 0) {
                setErrMsg(res, "用户ID参数缺失");
                return res;
            }
            int intEmployeeId = loginEmployee.getId();

            //页数
            Integer page = pipelineContext.getRequest().getInt("page");
            //每页记录数
            Integer pageSize = pipelineContext.getRequest().getInt("pageSize");

            if (page == 0) {
                page = Page_Default;
            }
            if (pageSize == 0) {
                pageSize = PageSize_Default;
            }

            // 负责的小区
            if ("inChargeCommunity".equals(strType)) {
                IntSearch ownerIdSearch = new IntSearch("ownerId");
                ownerIdSearch.setSearchValue(String.valueOf(intEmployeeId));
                PaginationDescribe<RedstarCommunity> inChargeCommunityResult =
                        (PaginationDescribe<RedstarCommunity>) dispatchDriver.getRedstarCommunityManager().searchBeanPage(page, pageSize, ownerIdSearch, "updateDate", Boolean.FALSE);
                List<RedstarCommunity> redstarCommunityList = inChargeCommunityResult.getCurrentRecords();
                ((SimplePaginationDescribe) inChargeCommunityResult).setCurrentRecords(redstarCommunityList);

                res.addKey("result", inChargeCommunityResult);
            } else if ("updateCommunity".equals(strType)) {  // 完善的小区（非商场员工只调用这个接口）
                IntSearch updateIdSearch = new IntSearch("updateEmployeeId");
                updateIdSearch.setSearchValue(String.valueOf(intEmployeeId));

                PaginationDescribe<RedstarCommunityUpdateLog> updateCommunityResult =
                        (PaginationDescribe<RedstarCommunityUpdateLog>) dispatchDriver.getRedstarCommunityUpdateLogManager().searchBeanPage(page, pageSize, updateIdSearch, "updateDate", Boolean.FALSE);
                List<RedstarCommunityUpdateLog> redstarCommunityUpdateLogList = updateCommunityResult.getCurrentRecords();
                ((SimplePaginationDescribe) updateCommunityResult).setCurrentRecords(redstarCommunityUpdateLogList);

                res.addKey("result", updateCommunityResult);
            }
            setSuccessMsg(res);
        } catch (Exception e) {
            setUnknowException(e, res);
        }
        return res;
    }

    /**
     * //查询周边小区列表
     * @param strType  allAroundCommunity=全部 occupyCommunity=可抢占 predistributionCommunity=预分配
     * @param longitude 经度
     * @param latitude 纬度
     * @param provinceCode
     * @param cityCode
     * @param limitM 查询小区半径（米）
     * @return
     */
    @RequestMapping(value = "/aroundList/{type}", method = RequestMethod.POST)
    @ResponseBody
    public Response aroundList(@PathVariable("type") String strType, String longitude, String latitude,
                               String provinceCode, String cityCode, String limitM) {
        PipelineContext pipelineContext = this.buildPipelineContent();
        Response res = pipelineContext.getResponse();

        try {
            // 查询参数设定
            // 登陆EmployeeID的商场ID获得（非员工没有商场ID）
            // TODO 登陆逻辑修改
            Employee loginEmployee = this.getEmployeeromSession();
            int intOwnerMallId = loginEmployee.getId();

            // 所在经纬度,省市code判断
            if (StringUtil.isInvalid(latitude) || StringUtil.isInvalid(longitude)
                    || StringUtil.isInvalid(provinceCode) || StringUtil.isInvalid(cityCode)) {
                setErrMsg(res, "经纬度参数缺失");
                return res;
            }

            // 查询小区范围如果缺失，设定默认查询范围为5公里
            if (StringUtil.isInvalid(limitM)) {
                limitM = "5000";
            }

            //页数
            Integer page = pipelineContext.getRequest().getInt("page");
            //每页记录数
            Integer pageSize = pipelineContext.getRequest().getInt("pageSize");

            if (page == 0) {
                page = Page_Default;
            }
            if (pageSize == 0) {
                pageSize = PageSize_Default;
            }

            // 预分配小区(所属商场下无管理者的小区)
            if ("predistributionCommunity".equals(strType)) {

                int intLimtM = Integer.parseInt(limitM);
                StringBuffer sb = new StringBuffer();

                sb.append("Select c.id, c.name,c.address,c.reclaimStatus,c.reclaimCompleteDate, ");
                sb.append(" round(6378.138 * 2 * asin(  ");
                sb.append(" sqrt( pow(sin((c.latitude * pi() / 180 - ?*pi() / 180) / 2),2) + cos(c.latitude * pi() / 180) ");
                sb.append(" * cos(?*pi() / 180) * pow(  ");
                sb.append(" sin((c.longitude * pi() / 180 - ?*pi()/180) / 2),2))) * 1000) AS distance   ");
                sb.append(" FROM xiwa_redstar_community c where c.longitude>0 and c.latitude>0 and c.ownerMallId=? HAVING distance < ? ORDER BY distance ");

                String querySQL = sb.toString();

                List<Object> paramsList = new ArrayList<Object>();
                paramsList.add(Double.parseDouble(longitude));
                paramsList.add(Double.parseDouble(longitude));
                paramsList.add(Double.parseDouble(latitude));
                paramsList.add(intOwnerMallId);
                paramsList.add(intLimtM);

                //搜索结果（以员工GPS位置为圆心半径intLimtM内所有该员工所属商场下小区列表）
                List lsAroundCommunity = redstarCommonManager.excuteBySql(querySQL, paramsList);
                List<HashMap> lsAroundCommunitys = new ArrayList<HashMap>();

                // 查询结果list转化成输出对象
                for (int i = 0; i < lsAroundCommunity.size(); i++) {
                    Object[] objAd = (Object[]) lsAroundCommunity.get(i);
                    HashMap hmADComObj = new HashMap();
                    hmADComObj.put("communityId", objAd[0]);
                    hmADComObj.put("name", objAd[1]);
                    hmADComObj.put("address", objAd[2]);
                    hmADComObj.put("reclaimStatus", objAd[3]);
                    hmADComObj.put("reclaimCompleteDate", objAd[4]);
                    hmADComObj.put("distance", objAd[5]);

                    lsAroundCommunitys.add(hmADComObj);
                }

                res.addKey("result", lsAroundCommunitys);
            } else if ("occupyCommunity".equals(strType)) {  // 可抢的小区

                int intLimtM = Integer.parseInt(limitM);
                StringBuffer sb = new StringBuffer();

                sb.append("Select c.id, c.name,c.address, ");
                sb.append(" round(6378.138 * 2 * asin(  ");
                sb.append(" sqrt( pow(sin((c.latitude * pi() / 180 - ?*pi() / 180) / 2),2) + cos(c.latitude * pi() / 180) ");
                sb.append(" * cos(?*pi() / 180) * pow(  ");
                sb.append(" sin((c.longitude * pi() / 180 - ?*pi()/180) / 2),2))) * 1000) AS distance   ");
                sb.append(" FROM xiwa_redstar_community c WHERE c.longitude>0 AND c.latitude>0 AND c.reclaimStatus = 0 AND c.reclaimCompleteDate < ? HAVING distance < ? ORDER BY distance ");

                String querySQL = sb.toString();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strSysytemDateTime = df.format(System.currentTimeMillis());

                List<Object> paramsList = new ArrayList<Object>();
                paramsList.add(Double.parseDouble(longitude));
                paramsList.add(Double.parseDouble(longitude));
                paramsList.add(Double.parseDouble(latitude));
                paramsList.add(strSysytemDateTime);
                paramsList.add(intLimtM);

                //搜索结果（以员工GPS位置为圆心半径intLimtM内所有未分配小区列表）
                List lsAroundCommunity = redstarCommonManager.excuteBySql(querySQL, paramsList);
                List<HashMap> lsAroundCommunitys = new ArrayList<HashMap>();

                // 查询结果list转化成输出对象
                for (int i = 0; i < lsAroundCommunity.size(); i++) {
                    Object[] objAd = (Object[]) lsAroundCommunity.get(i);
                    HashMap hmADComObj = new HashMap();
                    hmADComObj.put("communityId", objAd[0]);
                    hmADComObj.put("name", objAd[1]);
                    hmADComObj.put("address", objAd[2]);
                    hmADComObj.put("distance", objAd[3]);

                    lsAroundCommunitys.add(hmADComObj);
                }

                res.addKey("result", lsAroundCommunitys);
            } else if ("allAroundCommunity".equals(strType)) {  // 周边所有的小区（非商场员工调用接口）

                int intLimtM = Integer.parseInt(limitM);
                StringBuffer sb = new StringBuffer();

                sb.append("Select c.id, c.name,c.address, ");
                sb.append(" round(6378.138 * 2 * asin(  ");
                sb.append(" sqrt( pow(sin((c.latitude * pi() / 180 - ?*pi() / 180) / 2),2) + cos(c.latitude * pi() / 180) ");
                sb.append(" * cos(?*pi() / 180) * pow(  ");
                sb.append(" sin((c.longitude * pi() / 180 - ?*pi()/180) / 2),2))) * 1000) AS distance   ");
                sb.append(" FROM xiwa_redstar_community c WHERE c.longitude>0 AND c.latitude>0 HAVING distance < ? ORDER BY distance ");

                String querySQL = sb.toString();

                List<Object> paramsList = new ArrayList<Object>();
                paramsList.add(Double.parseDouble(longitude));
                paramsList.add(Double.parseDouble(longitude));
                paramsList.add(Double.parseDouble(latitude));
                paramsList.add(intLimtM);

                //搜索结果（以员工GPS位置为圆心半径intLimtM内所有小区列表）
                List lsAllAroundCommunity = redstarCommonManager.excuteBySql(querySQL, paramsList);
                List<HashMap> lsAllAroundCommunitys = new ArrayList<HashMap>();

                // 查询结果list转化成输出对象
                for (int i = 0; i < lsAllAroundCommunity.size(); i++) {
                    Object[] objAllCommunity = (Object[]) lsAllAroundCommunity.get(i);
                    HashMap hmAllComObj = new HashMap();
                    hmAllComObj.put("communityId", objAllCommunity[0]);
                    hmAllComObj.put("name", objAllCommunity[1]);
                    hmAllComObj.put("address", objAllCommunity[2]);
                    hmAllComObj.put("distance", objAllCommunity[3]);

                    lsAllAroundCommunitys.add(hmAllComObj);
                }

                res.addKey("result", lsAllAroundCommunitys);
            }

            setSuccessMsg(res);
        } catch (Exception e) {
            setUnknowException(e, res);
        }
        return res;
    }


    //查询小区详细信息
    @RequestMapping(value = "/{id}")
    @ResponseBody
    public Response dataItem(@PathVariable("id") Integer id) {
        PipelineContext pipelineContext = this.buildPipelineContent();
        Response res = pipelineContext.getResponse();
        try {
            RedstarCommunity community = (RedstarCommunity) dispatchDriver.getRedstarCommunityManager().getBean(id);
            double occupanyRate;
            double inputRate;
            if (community.getAlreadyCheckAmount() != null && community.getRoomMount() != null && community.getRoomMount() > 0) {
                occupanyRate = DoubleUtil.div(community.getAlreadyCheckAmount(), community.getRoomMount(), 2);
                community.setOccupanyRate(RateUtil.getDoubleValue(occupanyRate * 100));
            } else {
                community.setOccupanyRate(0.0);
            }
            if (community.getAlreadyInputAmount() != null && community.getRoomMount() != null && community.getRoomMount() > 0) {
                inputRate = DoubleUtil.div(community.getAlreadyInputAmount(), community.getRoomMount(), 2);
                community.setInputRate(RateUtil.getDoubleValue(inputRate * 100));
            } else {
                community.setInputRate(0.0);
            }
            res.setCode(HTTP_SUCCESS_CODE);
            res.addKey("community", community);
        } catch (Exception e) {
            setErrMsg(res, "没有数据");
        }
        return res;
    }


    /**
     * 返回某个小区的栋单元列表
     * <p/>
     * 参数 communityId
     * 表 xiwa_redstar_community_building
     *
     * @return response
     */
    @RequestMapping(value = "/building/list", method = RequestMethod.POST)
    @ResponseBody
    public Response buildingList() {
        PipelineContext pipelineContext = buildPipelineContent();
        Response response = pipelineContext.getResponse();
        int communityId = pipelineContext.getRequest().getInt("communityId");
        //communityId 输入为空，为0，格式不符时 报错
        if (communityId == 0) {
            response.setCode(FORM_ERROR_CODE);
            response.setOk(false);
            response.setMessage("communityId输入有误");
            return response;
        }

        try {
            List<RedstarCommunityBuilding> redstarCommunityBuildings = dispatchDriver.getRedstarCommunityBuildingManager().getBeanListByColumn("communityId", communityId);
            response.addKey("redstarCommunityBuildings", redstarCommunityBuildings);

        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(40901);
            response.setOk(false);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    /**
     * 添加栋
     * <p/>
     * 再某个小区下面添加一个栋单元
     * <p/>
     * 栋数据表：xiwa_redstar_community_building
     *
     * @return response
     */
    @RequestMapping(value = "/building/add", method = RequestMethod.POST)
    @ResponseBody
    public Response buildingAdd() {
        PipelineContext pipelineContext = this.buildPipelineContent();
        Response response = pipelineContext.getResponse();

        //小区ID 和 栋的姓名
        int communityId = pipelineContext.getRequest().getInt("communityId");
        String buildingName = pipelineContext.getRequest().getString("buildingName");

        int roomAmount = pipelineContext.getRequest().getInt("roomAmount");//住宅数
        int unitAmount = pipelineContext.getRequest().getInt("unitAmount");//单元数
        int floorAmount = pipelineContext.getRequest().getInt("floorAmount");//楼层数

        if (roomAmount <= 0) {
            setErrCodeAndMsg(response, -1001, "住宅数不能为空");
            return response;
        }

        if (floorAmount <= 0) {
            setErrCodeAndMsg(response, -1001, "楼层数不能为空");
            return response;
        }
        if (communityId == 0) {
            response.setCode(FORM_ERROR_CODE);
            response.setOk(false);
            response.setMessage("communityId不能为空");
            return response;
        }

        if (StringUtils.isBlank(buildingName)) {
            response.setOk(false);
            response.setMessage("buildingName不能为空");
            response.setCode(FORM_ERROR_CODE);
            return response;
        }

        RedstarCommunityBuilding redstarCommunityBuilding = new RedstarCommunityBuilding();

        redstarCommunityBuilding.setCommunityId(communityId);
        redstarCommunityBuilding.setBuildingName(buildingName);
        redstarCommunityBuilding.setRoomAmount(roomAmount);
        redstarCommunityBuilding.setUnitAmount(unitAmount);
        redstarCommunityBuilding.setFloorAmount(floorAmount);

        try {
            //Employee employee = null;    // 获取session中的登陆用户的employeeCode
            Employee employee = getEmployeeromSession();

            //用户ID,姓名，创建时间
            redstarCommunityBuilding.setCreateEmployeeId(employee.getId());
            redstarCommunityBuilding.setCreateXingMing(employee.getXingMing());
            redstarCommunityBuilding.setCreateDate(new Date());

            //添加进栋列表
            int buildingId = dispatchDriver.getRedstarCommunityBuildingManager().addBean(redstarCommunityBuilding);
            response.addKey("buildingId", buildingId);
            response.setMessage("添加成功");
            response.setOk(true);
            response.setCode(HTTP_SUCCESS_CODE);
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setCode(40101);
            response.setOk(false);
            response.setMessage(exception.getMessage());
            return response;

        }

        return response;
    }


    //编辑栋信息
    @RequestMapping(value = "/building/update", method = RequestMethod.POST)
    @ResponseBody
    public Response buildingUpdate() {
        PipelineContext pipelineContext = this.buildPipelineContent();
        Response response = pipelineContext.getResponse();

        Integer dataId = pipelineContext.getRequest().getInt("id");

        if (dataId <= 0) {
            setErrCodeAndMsg(response, -1001, "数据id缺失");
            return response;
        }

        //小区ID 和 栋的姓名

        String buildingName = pipelineContext.getRequest().getString("buildingName");
        int roomAmount = pipelineContext.getRequest().getInt("roomAmount");//住宅数
        int unitAmount = pipelineContext.getRequest().getInt("unitAmount");//单元数
        int floorAmount = pipelineContext.getRequest().getInt("floorAmount");//楼层数

        if (roomAmount <= 0) {
            setErrCodeAndMsg(response, -1001, "住宅数不能为空");
            return response;
        }
        if (unitAmount <= 0) {
            setErrCodeAndMsg(response, -1001, "单元数不能为空");
            return response;
        }
        if (floorAmount <= 0) {
            setErrCodeAndMsg(response, -1001, "楼层数不能为空");
            return response;
        }

        if (StringUtils.isBlank(buildingName)) {
            setErrCodeAndMsg(response, -1001, "buildingName不能为空");
            return response;
        }

        try {

            RedstarCommunityBuilding redstarCommunityBuilding = (RedstarCommunityBuilding) dispatchDriver.getRedstarCommunityBuildingManager().getBean(dataId);

            if (redstarCommunityBuilding == null) {
                setErrCodeAndMsg(response, -1001, "当前数据不存在");
                return response;
            }

            redstarCommunityBuilding.setBuildingName(buildingName);
            redstarCommunityBuilding.setRoomAmount(roomAmount);
            redstarCommunityBuilding.setUnitAmount(unitAmount);
            redstarCommunityBuilding.setFloorAmount(floorAmount);

            //添加进栋列表
            dispatchDriver.getRedstarCommunityBuildingManager().updateBean(redstarCommunityBuilding);
            response.setMessage("更新成功");
            response.setOk(true);
            response.setCode(HTTP_SUCCESS_CODE);
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setCode(40101);
            response.setOk(false);
            response.setMessage(exception.getMessage());
            return response;
        }

        return response;
    }


    /**
     * 根据栋单元ID删除栋单元数据
     * <p/>
     * 只能删除自己创建的栋/单元
     * 删除栋单元前查询该栋下面有无住宅，有则不允许删除
     * 是否有住宅查询表: xiwa_redstar_member
     *
     * @return response
     */
    @RequestMapping(value = "/building/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response buildingDelete() {
        PipelineContext pipelineContext = buildPipelineContent();
        Response response = pipelineContext.getResponse();
        String idStr = pipelineContext.getRequest().getString("ids");
        int communityId = pipelineContext.getRequest().getInt("communityId");

        try {
            //1.判断 栋ID 、 communityId 是否为空
            if (StringUtils.isBlank(idStr)) {
                throw new BusinessException(CommonExceptionType.ParameterError);
            }
            if (communityId == 0) {
                throw new BusinessException(CommonExceptionType.ParameterError);
            }

            // 获取session中的登陆用户的employeeCode
            Employee employee = getEmployeeromSession();
            if (employee == null) {
                throw new BusinessException(CommonExceptionType.NoSession);
            }

            //2.截取，获得string 数组
            String[] deleteArray = idStr.split(",");
            String strIds = "";      //*****
            //.获取ids数组
            if (deleteArray.length < 1) {
                throw new BusinessException(CommonExceptionType.ParameterError);
            }
            for (int i = 0; i < deleteArray.length; i++) {
                //获取集合的值 下标从0开始
                int id = DataUtil.getInt(deleteArray[i], 0);    //如果数组中有非数字时，这个方法直接将其排除掉了

                //判断是否符合删除条件，如果不符合，continue
                if (id <= 0) {  //ID为非整形数 和 0
                    continue;
                }

                try {
                    //封装查询参数
                    MultiSearchBean multiSearchBean = new MultiSearchBean();
                    IntSearch idIntSearch = new IntSearch("id");
                    idIntSearch.setSearchValue(String.valueOf(id));
                    multiSearchBean.addSearchBean(idIntSearch);
                    IntSearch communityIdIntSearch = new IntSearch("communityId");
                    communityIdIntSearch.setSearchValue(String.valueOf(communityId));
                    multiSearchBean.addSearchBean(communityIdIntSearch);

                    int createEmployeeId = employee.getId();

//                    IntSearch createEmployeeIdSearch = new IntSearch("createEmployeeId");
//                    createEmployeeIdSearch.setSearchValue(String.valueOf(createEmployeeId));
//                    multiSearchBean.addSearchBean(createEmployeeIdSearch);

                    //3.根据communityID 和 创建人ID 查询是否存在该栋 xiwa_redstar_community_building
                    List<RedstarCommunityBuilding> redstarCommunityBuildings = dispatchDriver.getRedstarCommunityBuildingManager().searchIdentify(multiSearchBean);

                    //判断如果存在该栋
                    if (redstarCommunityBuildings.size() > 0) {    //存在该栋
                        //3.1. 存在该栋   根据buildingID查找 单元表 xiwa_redstar_community_unit
                        for (RedstarCommunityBuilding redstarCommunityBuilding : redstarCommunityBuildings) {
                            int buildingId = redstarCommunityBuilding.getId();
                            IntSearch buildingIdSearch = new IntSearch("buildingId");
                            buildingIdSearch.setSearchValue(String.valueOf(buildingId));

                            //根据buildingID查找 该栋下是否存在unit
                            List<RedstarCommunityUnit> redstarCommunityUnits = redstarCommonManager.getDataList(RedstarCommunityUnit.class, buildingIdSearch);
                            if (redstarCommunityUnits.size() > 0) { //该栋下有单元存在
                                response.setOk(false);
                                response.setCode(DataUtil.getInt(CommunityExceptionType.DeleteInUse.getCode(), 0));
                                response.setMessage(CommunityExceptionType.DeleteInUse.getMessage());
                                continue;
                            }
                            if (id != 0) {
                                //下标i循环到最后一个时 得到栋存在的字符串
                                if (i + 1 == deleteArray.length) {
                                    strIds += id;
                                } else {
                                    strIds += id + ",";
                                }
                            }
                        }

                    } else {
                        continue;

                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                    response.setCode(401101);
                    response.setMessage(exception.getMessage());
                    response.setOk(false);
                    return response;
                }

            }


            if (StringUtil.isValid(strIds)) {
                int ids[] = new int[strIds.split(",").length];
                for (int i = 0; i < strIds.split(",").length; i++) {
                    ids[i] = Integer.parseInt(strIds.split(",")[i]);
                }
                if (ids.length == 0) {
                    throw new BusinessException(CommonExceptionType.DeleteNoExist);
                }
                //删除
                dispatchDriver.getRedstarCommunityBuildingManager().removeBeans(ids);
                setSuccessMsg(response, "删除成功");
            } else {
                throw new BusinessException(CommonExceptionType.ParameterError);
            }

        } catch (BusinessException e) {
            setBusinessException(e, response);
        } catch (Exception e) {
            setUnknowException(e, response);
        }
        return response;
    }


}
