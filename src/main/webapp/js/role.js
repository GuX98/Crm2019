function searchRoles() {
    $("#dg").datagrid("load",{
        roleName:$("#roleName").val()
    })
}

function openRelatePermissionDlg() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择角色进行授权!","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","只能选择一条角色进行授权!","info");
        return;
    }
    $("#rid").val(rows[0].id);
    loadModuleData();
    $("#dlg02").dialog("open");
}
function zTreeOnCheck() {
    var znodes=ztreeObj.getCheckedNodes(true);
    var moduleIds="moduleIds=";
    if(znodes.length>0){
        for(var i=0;i<znodes.length;i++){
            if(i<=znodes.length-2){
                moduleIds=moduleIds+znodes[i].id+"&moduleIds=";
            }else{
                moduleIds=moduleIds+znodes[i].id;
            }
        }
    }
    console.log(moduleIds);
    $("#moduleIds").val(moduleIds);
}

var ztreeObj;
function loadModuleData(){
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllsModuleDtos",
        data:"rid="+$("#rid").val(),
        dataType:"json",
        success:function(data){
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
            var zNodes =data;
            ztreeObj= $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
    });
}

function addPermission() {
    $.ajax({
        type:"post",
        url:ctx+"/role/addPermission",
        data:"rid="+$("#rid").val()+"&"+$("#moduleIds").val(),
        dataType:"json",
        success:function(data){
            console.log(data);
            if(data.code == 200){
                $.messager.alert("来自Crm",data.msg,"info");
                $("#moduleIds").val("");
                $("#rid").val("");
            }else{
                $.messager.alert("来自Crm",data.msg,"error");
            }
        }
    })
}

function openAddRoleDialog(){
    $("#dlg").dialog("open");
}