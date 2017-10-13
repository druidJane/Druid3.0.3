package com.xuanwu.mos.file.importer;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.domain.enums.CheckResult;
import com.xuanwu.mos.utils.Delimiters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 林泽强 on 2016/8/29.
 * 导入信息对象构建器
 */
public class ImportInfoBuild {

    private int importedCount;  //数据计数器
    private List<AbstractEntity> failed = new ArrayList<>();    //失败列表
    private Map<CheckResult, Integer> map = new HashMap<>();

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public List<AbstractEntity> getFailed() {
        return failed;
    }

    public void setFailed(List<AbstractEntity> failed) {
        this.failed = failed;
    }

    public Map<CheckResult, Integer> getMap() {
        return map;
    }

    public void setMap(Map<CheckResult, Integer> map) {
        this.map = map;
    }

    /**
     * 单个计数器
     * @param result
     * @param item
     */
    public void increment(CheckResult result, AbstractEntity item) {
        importedCount ++;
        Integer count = map.get(result);
        if(count == null) {
            map.put(result,new Integer(1));
        }else {
            map.put(result,new Integer(count.intValue() + 1));
        }
        if(result == CheckResult.Legal) {
            return;
        }
        //不合法情况
        item.setExtAttr1(getMessage(result));
        failed.add(item);
    }

    /**
     * 链表计数器
     * @param result
     * @param items
     */
    public void increment(CheckResult result, List<AbstractEntity> items) {
        importedCount += items.size();
        Integer count = map.get(result);
        if (count == null)
            map.put(result, new Integer(items.size()));
        else
            map.put(result, new Integer(count.intValue() + items.size()));
        if (result == CheckResult.Legal)
            return;
        //不合法加到失败列表
        for (AbstractEntity item : items) {
            item.setExtAttr1(getMessage(result));
            failed.add(item);
        }
    }

    public String getHandleReport() {
        StringBuilder sb = new StringBuilder();
        int total = 0, success = 0, fail = 0;
        for (Map.Entry<CheckResult, Integer> entry : map.entrySet()) {
            CheckResult key = entry.getKey();
            int count = entry.getValue();
            if (key == CheckResult.Legal) {
                success += count;
            } else {
                sb.append(getMessage(key, count));
                sb.append(Delimiters.SICOLON);
                fail += count;
            }
            total += count;
        }
        if (fail > 0) {
            sb.insert(0, ("导入失败数：" + fail + "，其中："));
        }
        if (success > 0) {
            sb.insert(0, ("导入成功数：" + success + Delimiters.SICOLON));
        }
        sb.insert(0, ("<b>文件总记录数：" + total + "</b>" + Delimiters.SICOLON));
        return sb.toString();
    }
    //批量错误信息
    private String getMessage(CheckResult result, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage(result)).append("数: ").append(count);
        return sb.toString();
    }

    //错误信息
    private String getMessage(CheckResult result) {
        switch (result) {
            case IllegalPhone:
                return "手机号码无效";
            case IllegalUserExtNum:
                return "用户扩展码无效";
            case IllegalPlatform:
            	return "错误的平台名称";
            case IllegalRole:
            	return "错误的角色名称";
            case IllegalEmail:
            	return "邮件错误";
            case IllegalDeptNo:
                return "部门编号无效";
            case IllegalDeptNoNotInDataScope:
            	return "部门编号不在权限范围内";
            case IllegalLinkMan:
                return "用户名称不合法";
            case IllegalPassword:
                return "密码无效";
            case IllegalUserName:
                return "用户账号格式错误";
            case DeptNotExsit:
                return "该用户的权限范围内部门编号不存在";
            case UserNameRepate:
                return "已存在相同用户账号";
            case UserExtNumRepate:
                return "用户扩展码重复";
            case IllegalUserBizNames:
                return "业务能力不在部门权限范围";
            case IllegalUserRoleNames:
                return "角色不在部门权限范围";
            case ContactNameEmpty:
                return "联系人姓名为空";
            case ContactPhoneExists:
                return "联系人手机号码重复";
            case ContactIllegalGroupName:
                return "非法群组名称";
            case ContactIllegalName:
                return "非法联系人名称";
            case ContactIllegalIdentifier:
                return "非法联系人编号";
            case ContactIllegalRemark:
                return "非法联系人备注";
            case IllegalKeyword:
                return "非法关键字无效";
            case KeywordExists:
                return "非法关键字已经存在";
            case KeyWordRepate:
                return "非法关键字重复";
            case KeywordTooLong:
                return "非法关键字长度太长";
            case IllegalKeywordTarget:
                return "非法关键字所属通道无效";
            case IllegalKeywordType:
                return "非法关键字类型不合法";
            case BlankPhone:
                return "手机号码为空";
            case BlacklistRemarkTooLong:
                return "备注信息太长";
            case IllegalBlacklistType:
                return "黑名单类型无效";
            case IllegalBlacklistTarget:
                return "黑名单所属对象无效";
            case BlacklistExists:
                return "黑名单已经存在";
            case BlacklistEntNExists:
                return  "黑名单所属企业不存在";
             case BlacklistUserNExists:
                return  "黑名单所属用户不存在";
             case BlacklistBizNExists:
                return  "黑名单所属业务类型不存在";
            case BlacklistRepate:
                return "黑名单重复";
            case NoBlacklistPhone:
                return "没有符合规则的黑名单号";
            case IllegalSex:
                return "【性别】导入内容系统无法正确识别或内容为空";
            case IllegalVip:
                return "VIP属性不合法或为空";
            case ChannelNotExsit:
                return "白名单通道不存在";
            case WhitelistExists:
                return "白名单已经存在";
            case SelfNameEmpty:
                return "查询指令不能为空";
            case SelfNameTooLong:
                return "查询指令长度不能超过20个字符";
            case SelfNameExsit:
                return "查询指令已存在";
            case SelfNameIllegal:
                return "查询指令不合法";
            case SelfSubjectTooLong:
                return "信息主题长度不能超过100个字符";
            case SelfAutoRepliesEmpty:
                return "信息内容不能为空";
            case SelfAutoRepliesTooLong:
                return "信息内容不能超过1000个字符";
            case SelfHandleUserNotExsit:
                return "信息收发人非有效账号";

            case UserAuditAccountEmpty:
                return "企业账号不能为空";
            case UserAuditAccountErr:
                return "企业账号错误";
            case UserAuditAuditingNumErr:
                return "审核基数格式错误";

            case NumberBlank:
                return "400号码为空";
            case NumberInvalid:
                return "400号码格式不正确";
            case NumberRepeated:
                return "400号码重复";
            case NumberNotIn:
                return "400号码通道不存在";
            case NumberOutOfExtendSize:
                return "400号码长度超过通道可扩展长度";
            case IllegalBlacklistAddOrDel:
                return "状态列信息不合法";
            case RegionSegmtPhoneBlank:
                return "区域号段为空 ";
            case RegionSegmtPhoneNumIllegal:
                return "区域号段必须是数字 ";
            case RegionSegmtPhoneZeroOrOneIllegal:
                return "区域号段必须已0或1开头";
            case RegionSegmtPhoneZeroIllegal:
                return "区域号段以0开头，必须为3位或4位 ";
            case RegionSegmtPhoneOneIllegal:
                return "区域号段以1开头，必须为7位 ";
            case RegionSegmtCarrierNotExist:
                return "运营商不存在 ";
            case RegionSegmtRegionNotExist:
                return "省不存在";
            case SendLimitSendNumNotDigit:
                return "发送限制发送数目不为数字"; /** 发送限制发送数目不为数字 **/
            case SendLimitSendTimeIntervalNotDigit:
                return "发送限制发送时间间隔不为数字"; /** 发送限制发送时间间隔不为数字 **/
            case RegionSegmtPhoneANDCarrierNotMatch:
                return "请以库中存在的运营商号段开头";
            case RegionSegmtRegionPhoneRepeat:
                return "该运营商的号码段已经存在";
            case   SendLimitSendNumThanMaxNum:
                return "发送限制发送数目必须大于0并且小于等于100000";
            case SendLimitSendTimeIntervalThanMaxNum:
                return "发送限制发送时间间隔必须大于0并且小于等于100000";
            case ChargeMoneyIllegal:
                return "仅允许输入小于等于【剩余信用额度】且不能为0，精确到小数点后四位";
            case ChargeAccountBalanceNotEnough:
                return "企业账户余额不足,无法充值";
            case NotFoundChildAccountName:
                return "计费账户名称不存在";
            case NotFoundParentChargeAccount:
                return "找不到企业总账户信息，无法完成充值";
            case ChargeMoneyFormatException:
                return "充值金额必须为数值";
            case ChargeWayNotHandledCharge:
                return "该计费账户对应的充值方式不是手动充值";
            case UserAccountTypeIllegal:
                return "账号类型为必填/错误";
            case UserNameTooLong:
                return "用户账号超出了字符长度限制";
            case LinkManTooLong:
                return "用户名称不能超过50个字符";
            case PasswordRuleLimit:
                return "密码长度限制为8~16位数字、大写或小写字母组合";
            case PhoneRuleLimit:
                return "手机号格式错误/手机号不存在运营商号段";
            case DeptExcludeBizType:
                return "该部门编号下不包含该业务类型";
            case UserIndentityBlueLimit:
                return "用户扩展码仅允许输入数字字符";
            case UserIdentityRepeat:
                return "用户扩展码已存在";
            case UserSignatureTooLong:
                return "用户签名不能超过30个字符";
            case IllegalUserSigLocation:
                return "签名位置不合法";
            case UserProtocolTypeIllegal:
                return "协议类型错误";
            case SrcPortIllegal:
                return "源端口格式错误";
            case CallbackAddressIllegal:
                return "IP地址格式错误/端口号非法";
            case CustomerSignatureIllegal:
                return "报备签名不合法";
            case SendSpeedIllegal:
                return "发送速度导入值非法";
            case linkNumIllegal:
                return "链接数导入值非法";
            case DeptNameIllegal:
                return "部门名称与部门编号没有对应关系";
            case SYSTEM_ERROR:
                return "系统异常";
            case IllegalBirthday:
            	return "出生日期格式错误/出生日期应小于等于当前系统日期";
            case IllegalVIP:
            	return "【VIP】导入内容系统无法正确识别";
            case DransparentSendIllegal:
                return "mos后台该企业账号的【是否透传】为否，所以不能导入【账号类型】为【透传】的用户";
            case PushAddressTooLong:
                return "上行推送地不能超过100个字符";
            case ReportPushAddressTooLong:
                return "状态报告推送地址不能超过100个字符";
            default:
                break;
        }
        return "";
    }

}
