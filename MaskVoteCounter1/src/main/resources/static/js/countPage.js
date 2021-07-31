/*
显示那个模块，用css样式类：hide决定即可。
* */

$(function () {
    $(".leftBtn").click(function () {
        $(this).addClass('active').siblings().removeClass('active');
    });


    $('#beginElect').click(function () {
        console.log("1");
        $.ajax({
            url:"/start",
            method: "post",
            dataType: "text",
            data: "gaugudfagu",
            success:function (result) {
                console.log("2");
                result = JSON.parse(result);
                $("#result").empty();
                var a = result[1];
                var b = result[2];
                var c = result[3];
                var d = result[4];
                var e = result[5];
                var f = result[6];
                var g = result[7];

                /**
                 * 循环打印map中的值
                 */
                // console.log(result);
                for (var i in result) {
                    (function (t, data) {
                        setTimeout(function () {
                            console.log(`这是第 ${t} 次，这是其他参数：${data}`);
                            $("#result").append(data).append("\n");
                        }, 1000*t);
                    })(i, result[i])
                }

                // setTimeout(function () {
                //     $("#result").append(a+"\n");
                // },1000);
                //
                // setTimeout(function () {
                //     $("#result").append(b+"\n");
                // },2000);
                //
                // setTimeout(function () {
                //     $("#result").append(c+"\n");
                // },3000);
                //
                // setTimeout(function () {
                //     $("#result").append(d+"\n");
                // },4000);
                //
                // setTimeout(function () {
                //     $("#result").append(e+"\n");
                // },5000);
                //
                // setTimeout(function () {
                //     $("#result").append(f+"\n");
                // },6000);
                //
                // setTimeout(function () {
                //     $("#result").append(g+"\n");
                // },7000);

            }
        });
    });

    $('#countSelfResult').click(function () {
        console.log("1");
        $.ajax({
            url:"/countYR",
            method: "post",
            dataType: "text",
            data: "gaugudfagu",
            success:function (result) {
                console.log("2");
                result = JSON.parse(result);
                $("#result").empty();
                // var aa = result[1];
                // var bb = result[2];
                // $("#result").html(aa+"\n"+bb);
                /**
                 * 循环打印map中的值
                 */
                // console.log(result);
                for (var i in result) {
                    (function (t, data) {
                        setTimeout(function () {
                            console.log(`这是第 ${t} 次，这是其他参数：${data}`);
                            $("#result").append(data).append("\n");
                        }, 1000*t);
                    })(i, result[i])
                }
            }
        });
    });


    $('#beginVote').click(function () {
        console.log("1");
        $.ajax({
            url:"/startUpload",
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


    $('#beginVote1').click(function () {
        console.log("1");
        $.ajax({
            url:"/startVote",
            method: "post",
            dataType: "text",
            data: "gaugudfagu",
            success:function (result) {
                console.log("2");
                result = JSON.parse(result);
                $("#result").empty();
                // var aa = result[1];
                // var bb = result[2];
                // $("#result").html(aa+"\n"+bb);
                /**
                 * 循环打印map中的值
                 */
                // console.log(result);
                for (var i in result) {
                    (function (t, data) {
                        setTimeout(function () {
                            console.log(`这是第 ${t} 次，这是其他参数：${data}`);
                            $("#result").append(data).append("\n");
                        }, 1000*t);
                    })(i, result[i])
                }
            }
        });
    });


    $('#beginSelfResult').click(function () {
        console.log("1");
        $.ajax({
            url:"/countYRVote",
            method: "post",
            dataType: "text",
            data: "gaugudfagu",
            success:function (result) {
                console.log("2");
                result = JSON.parse(result);
                $("#result").empty();
                // var aa = result[1];
                // var bb = result[2];
                // $("#result").html(aa+"\n"+bb);
                /**
                 * 循环打印map中的值
                 */
                // console.log(result);
                for (var i in result) {
                    (function (t, data) {
                        setTimeout(function () {
                            console.log(`这是第 ${t} 次，这是其他参数：${data}`);
                            $("#result").append(data).append("\n");
                        }, 1000*t);
                    })(i, result[i])
                }
            }
        });
    });


    $('#beginInvokeVote').click(function () {
        console.log("1");
        $.ajax({
            url:"/startUploadVote",
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


