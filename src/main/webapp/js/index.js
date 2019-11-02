function userLogin() {
    //获取前台参数
    var userName = $("#userName").val();
    var userPwd = $("#userPwd").val();
    //非空判断
    if(isEmpty(userName)){
        alert("用户名不能为空！");
        return;
    }
    if(isEmpty(userPwd)){
        alert("密码不能为空！");
        return;
    }
    //ajax
    var params = {};
    params.userName = userName;
    params.userPwd = userPwd;
    $.ajax({
        type:"post",
        url:ctx+"/user/userLogin",
        data:params,
        dataType:"json",
        success:function (data) {
            if(data.code == 200){
                alert(data.msg);
                $.cookie("userName",data.result.userName);
                $.cookie("trueName",data.result.trueName);
                $.cookie("userId",data.result.userId);

                //实现页面的挑战
                window.location.href="main";
            }else{
                alert(data.msg);
            }
        }
    });
}