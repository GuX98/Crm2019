function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}


//退出登录
function logout(){
    $.messager.confirm("退出","确定退出系统？",function (r) {
        if(r){
            setTimeout(function () {
                $.removeCookie("userName");
                $.removeCookie("trueName");
                $.removeCookie("userId");
                window.location.href = "index";
            },1234);
        }
    });
}

//打开修改密码对话框
function openPasswordModifyDialog(){
    $("#dlg").dialog("open");
}
//修改密码对话框取消按钮
function closePasswordModifyDialog(){
    $("#dlg").dialog("close");
}
//保存按钮
function modifyPassword(){
    $("#fm").form("submit",{
       url:ctx+"/user/updatePwd",
       onsubmit:function(){
           return $("#fm").form("validate");
       },
        success:function (data) {
            data = JSON.parse(data);//将data转成json格式
            if(data.code == 200){
                $.messager.alert("修改密码",data.msg,"info");
                setTimeout(function () {
                    $.removeCookie("userName");
                    $.removeCookie("trueName");
                    $.removeCookie("userId");
                    window.location.href="index";
                },2000);

            }else{
                $.messager.alert("修改密码",data.msg);
            }
        }
    });
}




