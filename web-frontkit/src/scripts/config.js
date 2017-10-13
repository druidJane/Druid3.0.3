var context = "/mos"; //上下文目录
var tmp = '/tmp/'; // 显示临时图片，音频，视频目录
var serviceRoot = context + "/service/"; //API根路径
var resRoot = ""; //静态资源根路径
var webversion = "v5.0" //版本号

var loginPage = context + "/login.html";
var indexPage = context + "/#/";

var xpath = {
    service: function(path) {
        return serviceRoot + path;
    },
    res: function(path) {
        return resRoot + path;
    },
    // 在此处配置显示图片的目录的具体url
    imgUrl: '',
    // 此处配置是否已经配置显示图片的url，1为已经配置，0为尚未配置
    isImgFlag:0
};
