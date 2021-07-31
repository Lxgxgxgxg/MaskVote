$(function () {
    // //点击投票员
    // $('.voteTitle').click(function () {
    //     $(this).addClass('active1').siblings().removeClass('active2');
    //     $('.voteBox').removeClass('hide');
    //     $('.countBox').addClass('hide');
    // });
    // // //点击计票员
    // $('.countTitle').click(function () {
    //     $(this).addClass('active2').siblings().removeClass('active1');
    //     $('.countBox').removeClass('hide');
    //     $('.voteBox').addClass('hide');
    // });
    // //点击登陆
    // $('#login').click(function () {
    //     window.location = '计票主页.html';
    // });
    // //点击提交
    // $('#submit').click(function () {
    //     window.location = 'main.html';
    // });


    $('#counterRegister').click(function () {
        console.log("1");
        $.ajax({
            url:"/counterRegister",
            method: "post",
            dataType: "text",
            data: "gaugudfagu",
            success:function (result) {
                console.log("2");
                console.log(result);
                // this.text = result;
                alert(result);
                // $("#result").html(result);
            }
        });
    });


});
