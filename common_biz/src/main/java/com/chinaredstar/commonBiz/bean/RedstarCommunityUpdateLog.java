package com.chinaredstar.commonBiz.bean;

import com.xiwa.base.bean.Identified;

import java.util.Date;


/**
 * Created by StevenDong on 2016/9/21
 */
public class RedstarCommunityUpdateLog implements Identified {

    private int id;

    private String city;//  市

    private String province;//  省

    private int merchantId = 0;//  商户ID

    private String name;//  名字

    private String area;//  区

    private String provinceCode;//  省代码

    private String cityCode;//  市代码

    private String areaCode;//  区代码

    private String address;//  地址

    private String logo;//  LOGO

    private boolean userOpen;//  用户是否开通 (true/false)

    private int currentNo = 0;//  /最新No值

    private String hotline;//  物业电话

    private String pickupAddress;//提货点

    private String distributionCharge;//配送费

    private String freeDistribution;//配送费满减规则

    private Integer areaMonut = 0;//  占地面积 （㎡）

    private Integer roomMount = 0;//  总户数 （户 ）

    private Integer buildingAmount = 0;//  总幢数  （幢/栋）

    private Integer alreadyCheckAmount = 0;//  已入住户数 （户）

    private Integer priceSection = 0;//  房价（元/㎡）

    private String buildingDate;//  建筑年代  （年

    private String developers;//  开发商

    private Integer createEmployeeId = 0;//  创建员工ID

    private Integer updateEmployeeId = 0;//  更新员工ID

    private String createXingMing;//  创建人姓名

    private String updateEmployeeXingMing;//  修改员工姓名

    private Date updateDate;//  修改日期

    private Double longitude = 0.0;//  经度    (度)

    private Double latitude = 0.0;//  维度    (度)

    private Date createDate;//  创建时间


    private Integer ownerId = 0;//  分配员工id

    private String ownerXingMing;//  分配员工姓名

    private String source;//  数据来源

    private Integer alreadyInputAmount = 0;//   已录入总数 （户）

    private Double inputRate = 0.0;//  录入率

    private Double occupanyRate = 0.0;//  入住率

    private Integer ownerMallId = 0;//  小区所属商场ID

    private String ownerMallName;// 小区所属商场名称

    private String shortName;//  小区别称

    private String constructionTypes;//  建筑类型，高层，别墅，商住

    private String renovations;//   交房装修，毛培，精装

    private String deliveryTime;//  交房时间

    private String propertyName;//  楼盘名称

    private Integer dataBelong = 2; //  数据拥有者

    private Integer reclaimStatus = 0; //  认领状态

    private Integer completeStatus = 0;//  完善状态

    private Integer communityId; // 小区表中小区ID

    private Integer reviewStatus = 0; //审核状态

    private String editColumnName; //本次修改的字段名

    private Date auditShowDate; //审核显示时间（方便前台展示非数据库字段）

    public RedstarCommunityUpdateLog() {

    }

    public RedstarCommunityUpdateLog(RedstarCommunity community) {

        this.setCommunityId(community.getId());
        this.setAddress(community.getAddress());
        this.setAlreadyCheckAmount(community.getAlreadyCheckAmount());
        this.setAlreadyInputAmount(community.getAlreadyInputAmount());
        this.setArea(community.getArea());
        this.setAreaCode(community.getAreaCode());
        this.setAreaMonut(community.getAreaMonut());
        this.setBuildingAmount(community.getBuildingAmount());
        this.setBuildingDate(community.getBuildingDate());
        this.setCity(community.getCity());
        this.setCityCode(community.getCityCode());
        this.setCompleteStatus(community.getCompleteStatus());
        this.setConstructionTypes(community.getConstructionTypes());
        this.setCreateDate(community.getCreateDate());
        this.setCreateEmployeeId(community.getCreateEmployeeId());
        this.setCreateXingMing(community.getCreateXingMing());
        this.setCurrentNo(community.getCurrentNo());
        this.setDataBelong(community.getDataBelong());
        this.setDeliveryTime(community.getDeliveryTime());
        this.setDevelopers(community.getDevelopers());
        this.setDistributionCharge(community.getDistributionCharge());
        this.setFreeDistribution(community.getFreeDistribution());
        this.setHotline(community.getHotline());
        this.setInputRate(community.getInputRate());
        this.setLatitude(community.getLatitude());
        this.setLogo(community.getLogo());
        this.setLongitude(community.getLongitude());
        this.setMerchantId(community.getMerchantId());
        this.setName(community.getName());
        this.setOccupanyRate(community.getOccupanyRate());
        this.setOwnerId(community.getOwnerId());
        this.setOwnerMallId(community.getOwnerMallId());
        this.setOwnerMallName(community.getOwnerMallName());
        this.setOwnerXingMing(community.getOwnerXingMing());
        this.setPickupAddress(community.getPickupAddress());
        this.setPriceSection(community.getPriceSection());
        this.setPropertyName(community.getPropertyName());
        this.setProvince(community.getProvince());
        this.setProvinceCode(community.getProvinceCode());
        this.setReclaimStatus(community.getReclaimStatus());
        this.setRenovations(community.getRenovations());
        this.setReviewStatus(community.getReviewStatus());
        this.setRoomMount(community.getRoomMount());
        this.setShortName(community.getShortName());
        this.setSource(community.getSource());
        this.setUpdateDate(community.getUpdateDate());
        this.setUpdateEmployeeId(community.getUpdateEmployeeId());
        this.setUpdateEmployeeXingMing(community.getUpdateEmployeeXingMing());
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isUserOpen() {
        return userOpen;
    }

    public void setUserOpen(boolean userOpen) {
        this.userOpen = userOpen;
    }

    public int getCurrentNo() {
        return currentNo;
    }

    public void setCurrentNo(int currentNo) {
        this.currentNo = currentNo;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDistributionCharge() {
        return distributionCharge;
    }

    public void setDistributionCharge(String distributionCharge) {
        this.distributionCharge = distributionCharge;
    }

    public String getFreeDistribution() {
        return freeDistribution;
    }

    public void setFreeDistribution(String freeDistribution) {
        this.freeDistribution = freeDistribution;
    }

    public Integer getAreaMonut() {
        return areaMonut;
    }

    public void setAreaMonut(Integer areaMonut) {
        this.areaMonut = areaMonut;
    }

    public Integer getRoomMount() {
        return roomMount;
    }

    public void setRoomMount(Integer roomMount) {
        this.roomMount = roomMount;
    }

    public Integer getBuildingAmount() {
        return buildingAmount;
    }

    public void setBuildingAmount(Integer buildingAmount) {
        this.buildingAmount = buildingAmount;
    }

    public Integer getAlreadyCheckAmount() {
        return alreadyCheckAmount;
    }

    public void setAlreadyCheckAmount(Integer alreadyCheckAmount) {
        this.alreadyCheckAmount = alreadyCheckAmount;
    }

    public Integer getPriceSection() {
        return priceSection;
    }

    public void setPriceSection(Integer priceSection) {
        this.priceSection = priceSection;
    }

    public String getBuildingDate() {
        return buildingDate;
    }

    public void setBuildingDate(String buildingDate) {
        this.buildingDate = buildingDate;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public Integer getCreateEmployeeId() {
        return createEmployeeId;
    }

    public void setCreateEmployeeId(Integer createEmployeeId) {
        this.createEmployeeId = createEmployeeId;
    }

    public Integer getUpdateEmployeeId() {
        return updateEmployeeId;
    }

    public void setUpdateEmployeeId(Integer updateEmployeeId) {
        this.updateEmployeeId = updateEmployeeId;
    }

    public String getCreateXingMing() {
        return createXingMing;
    }

    public void setCreateXingMing(String createXingMing) {
        this.createXingMing = createXingMing;
    }

    public String getUpdateEmployeeXingMing() {
        return updateEmployeeXingMing;
    }

    public void setUpdateEmployeeXingMing(String updateEmployeeXingMing) {
        this.updateEmployeeXingMing = updateEmployeeXingMing;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerXingMing() {
        return ownerXingMing;
    }

    public void setOwnerXingMing(String ownerXingMing) {
        this.ownerXingMing = ownerXingMing;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getAlreadyInputAmount() {
        return alreadyInputAmount;
    }

    public void setAlreadyInputAmount(Integer alreadyInputAmount) {
        this.alreadyInputAmount = alreadyInputAmount;
    }

    public Double getInputRate() {
        return inputRate;
    }

    public void setInputRate(Double inputRate) {
        this.inputRate = inputRate;
    }

    public Double getOccupanyRate() {
        return occupanyRate;
    }

    public void setOccupanyRate(Double occupanyRate) {
        this.occupanyRate = occupanyRate;
    }

    public Integer getOwnerMallId() {
        return ownerMallId;
    }

    public void setOwnerMallId(Integer ownerMallId) {
        this.ownerMallId = ownerMallId;
    }

    public String getOwnerMallName() {
        return ownerMallName;
    }

    public void setOwnerMallName(String ownerMallName) {
        this.ownerMallName = ownerMallName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getConstructionTypes() {
        return constructionTypes;
    }

    public void setConstructionTypes(String constructionTypes) {
        this.constructionTypes = constructionTypes;
    }

    public String getRenovations() {
        return renovations;
    }

    public void setRenovations(String renovations) {
        this.renovations = renovations;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getDataBelong() {
        return dataBelong;
    }

    public void setDataBelong(Integer dataBelong) {
        this.dataBelong = dataBelong;
    }

    public Integer getReclaimStatus() {
        return reclaimStatus;
    }

    public void setReclaimStatus(Integer reclaimStatus) {
        this.reclaimStatus = reclaimStatus;
    }

    public Integer getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(Integer completeStatus) {
        this.completeStatus = completeStatus;
    }

    public Date getAuditShowDate() {
        return auditShowDate;
    }

    public void setAuditShowDate(Date auditShowDate) {
        this.auditShowDate = auditShowDate;
    }


    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getEditColumnName() {
        return editColumnName;
    }

    public void setEditColumnName(String editColumnName) {
        this.editColumnName = editColumnName;
    }
}
