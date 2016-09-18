package com.chinaredstar.commonBiz.bean.work;

import com.xiwa.base.bean.Identified;

public class RedstarRestaurantDepartment implements Identified {

    private int id;

    private int restaurantId;

    private String restaurantName;

    private int departmentId;

    private String departmentName;

    private String departmentCode;

    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xiwa_redstar_restaurant_department.restaurantId
     *
     * @param restaurantId the value for xiwa_redstar_restaurant_department.restaurantId
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xiwa_redstar_restaurant_department.restaurantName
     *
     * @return the value of xiwa_redstar_restaurant_department.restaurantName
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public String getRestaurantName() {
        return restaurantName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xiwa_redstar_restaurant_department.restaurantName
     *
     * @param restaurantName the value for xiwa_redstar_restaurant_department.restaurantName
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName == null ? null : restaurantName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xiwa_redstar_restaurant_department.departmentId
     *
     * @return the value of xiwa_redstar_restaurant_department.departmentId
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xiwa_redstar_restaurant_department.departmentId
     *
     * @param departmentId the value for xiwa_redstar_restaurant_department.departmentId
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xiwa_redstar_restaurant_department.departmentName
     *
     * @return the value of xiwa_redstar_restaurant_department.departmentName
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xiwa_redstar_restaurant_department.departmentName
     *
     * @param departmentName the value for xiwa_redstar_restaurant_department.departmentName
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xiwa_redstar_restaurant_department.departmentCode
     *
     * @return the value of xiwa_redstar_restaurant_department.departmentCode
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xiwa_redstar_restaurant_department.departmentCode
     *
     * @param departmentCode the value for xiwa_redstar_restaurant_department.departmentCode
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode == null ? null : departmentCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xiwa_redstar_restaurant_department.remark
     *
     * @return the value of xiwa_redstar_restaurant_department.remark
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xiwa_redstar_restaurant_department.remark
     *
     * @param remark the value for xiwa_redstar_restaurant_department.remark
     *
     * @mbggenerated Fri Jul 08 13:06:41 CST 2016
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}