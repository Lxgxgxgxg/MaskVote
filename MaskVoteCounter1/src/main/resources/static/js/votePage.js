/*
显示那个模块，用css样式类：hide决定即可。
* */

$(function () {
    // 左侧菜单点击
    // $(".leftBtn").click(function () {
    //     $(this).addClass('active').siblings().removeClass('active');
    // });

    // 选举事务点击
    // $(".voteMater").click(function () {
    //     $(".votePage").removeClass("hide").siblings().addClass("hide");
    // });

    // 点击跳转投票主页
    $('#elect').click(function () {
        window.location = 'electMain.html';
    });

    //点击跳转表决主页
    $('#vote').click(function () {
        window.location = 'voteMain.html';
    });

    //点击跳转到投票结果页面
    $('#electResult').click(function () {
        window.location = 'electResultPage.html';
    });

    //跳转到表决的结果页面
    $('#voteResult').click(function () {
        console.log(1)
        window.location = 'voteResultPage.html';
    });

    // document.all.label.style.display ="none";

});
