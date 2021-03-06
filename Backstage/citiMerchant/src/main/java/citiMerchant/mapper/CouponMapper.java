package citiMerchant.mapper;

import citiMerchant.vo.UserCoupon;
import citiMerchant.vo.UserCoupon_record;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper {

    //property 'state' can only be 'UNUSED' or 'USED'

    final String getCouponsByUserID = "SELECT * FROM user_coupon " +
            "WHERE userID = #{userID} AND state = 'UNUSED' ORDER BY getTime DESC";
    final String get_USED_Coupon = "SELECT * FROM user_coupon " +
            "WHERE userID = #{userID} AND state = 'USED' ORDER BY useTime DESC";
    final String get_OVERDUED_Coupon = "SELECT * FROM user_coupon " +
            "WHERE userID = #{userID} AND state = 'OVERDUED' ORDER BY getTime DESC";
    final String getCouponsBy_UserID_AND_ItemID = "SELECT * FROM user_coupon " +
            "WHERE userID = #{userID} AND ItemID = #{ItemID} AND state = 'UNUSED' ORDER BY getTime DESC";
    final String addUserCoupon = "INSERT INTO user_coupon(couponID, userID, ItemID, state, getTime, useTime) " +
            "VALUES(NULL, #{userID}, #{ItemID}, #{state}, now(), null)";
    final String deleteOneUserCouponBy_UserID_AND_ItemID = "CALL user_coupon_update(#{IN_userID, mode = IN, jdbcType = VARCHAR}, #{IN_itemID, mode = IN, jdbcType = VARCHAR}, #{ifUsed, mode = OUT, jdbcType = INTEGER})";
    final String updateOneUserCoupon_By_UserID = "CALL user_coupon_overdue_update(#{IN_userID, mode = IN, jdbcType = VARCHAR})";


    @Select(getCouponsByUserID)
    List<UserCoupon> getCouponsByUserID(String userID);

    @Select(get_USED_Coupon)
    List<UserCoupon> get_USED_Coupon(String userID);

    @Select(get_OVERDUED_Coupon)
    List<UserCoupon> get_OVERDUED_Coupon(String userID);

    @Select(getCouponsBy_UserID_AND_ItemID)
    List<UserCoupon> getCouponsBy_UserID_AND_ItemID(@Param("userID") String userID, @Param("ItemID") String ItemID);

    @Insert(addUserCoupon)
    int addUserCoupon(UserCoupon userCoupon);

    @Insert(value = deleteOneUserCouponBy_UserID_AND_ItemID)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(UserCoupon_record.class)
    void deleteOneUserCouponBy_UserID_AND_ItemID(UserCoupon_record userCoupon_record);

    @Insert(value = updateOneUserCoupon_By_UserID)
    @Options(statementType = StatementType.CALLABLE)
    void updateOneUserCoupon_By_UserID(@Param("IN_userID") String IN_userID);


}
