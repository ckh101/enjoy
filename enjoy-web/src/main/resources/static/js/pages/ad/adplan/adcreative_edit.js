$(function () {
    $("#img_limit").html(adsenses[adsense]["tips"]);
    if(adcreative_type == "image"){
        $("#adcreative_img").attr('src',imgurl);
        $("#adcreative_img").show();
    }else if(adcreative_type == "video"){
        $("#adcreative_video_source").attr('src',imgurl);
        $("#adcreative_video").show();
    }
    initFileInput("adcreativeImg","adcreativeImgurl","adcreativeImgBtn","smartad/adcreativeImg");
});
function initFileInput(fileId,fileInputId,uploadbtn){
    $("#"+fileId).fileinput({
        language : 'zh',
        uploadUrl:"/smartad-web/admin/advertisers/uploadImage",
        uploadExtraData: function(previewId, index) {   //该插件可以向您的服务器方法发送附加数据。这可以通过uploadExtraData在键值对中设置为关联数组对象来完成。所以如果你有设置uploadExtraData={id:'kv-1'}，在PHP中你可以读取这些数据$_POST['id']
            return {aId: aId, adcreative_type:adcreative_type};
        },
        showCaption:false,
        showCancel:false,
        showPreview:true,
        browseLabel: '选择素材',
        showCaption:false,
        removeFromPreviewOnError:true,
        autoReplace:true,
        fileActionSettings:{showZoom:false,indicatorNew:""},
        showUploadedThumbs:false,
        frameClass:"",
        dropZoneEnabled:false,
        browseClass: 'btn btn-primary',
        removeLabel: '清空',
        browseIcon: '<i class="icon-plus22 position-left"></i> ',
        uploadIcon: '<i class="icon-file-upload position-left"></i> ',
        uploadLabel: '上传',
        uploadClass:'btn btn-default kv-fileinput-upload '+uploadbtn,
        removeClass: 'btn btn-danger btn-icon',
        removeIcon: '<i class="icon-cancel-square"></i> ',
        maxFilesNum: 1,
        allowedFileExtensions: ["jpg","png","jpeg","mp4"],
        layoutTemplates:{
            actionUpload:'',
            actionZoom:'',
            actionDownload:'',
            actionDelete:''
        }
    });
    $("#"+fileId).on("filecleared",function(event, data, msg){
        $("#adcreativeImgurl").val("");
        $("#adcreativeImageId").val("");
    });
    $("#"+fileId).on("fileuploaded", function(event, data, previewId, index) {
        $("."+uploadbtn).hide();
        $(".kv-upload-progress").hide();
        if(data.response.status == 1){
            $("#adcreativeImgurl").val(data.response.data.data.preview_url);
            $("#adcreativeImageId").val(data.response.data.data.image_id+"");
        }else{
            $.alertWarning(data.response.msg);
        }

    });
    $("#"+fileId).on('fileloaded', function(event, file, previewId, index, reader) {
        $("."+uploadbtn).hide();
        if(adcreative_type == "image"){
            var img = $('#'+previewId).find('img')[0];
            img.onload = function () {
                var width = img.width;
                var height= img.height;
                var size = file.size/1024;
                if(width != adsenses[adsense]["width"] || height != adsenses[adsense]["height"] ||size > adsenses[adsense]["size"]){
                    $.alertWarning("图片尺寸或大小不合格！")
                    return;
                }
                $("#adcreativeImgurl").val("");
                $("#adcreativeImageId").val("");
                $("."+uploadbtn).show();
                $("#adcreative_img").hide();
            };
        }else if(adcreative_type == "video"){
            var video = $('#'+previewId).find("video")[0];
            video.addEventListener('canplay', function () {
                var width = this.videoWidth;
                var height= this.videoHeight;
                var size = file.size/1024;
                if(width != adsenses[adsense]["width"] || height != adsenses[adsense]["height"] ||size > adsenses[adsense]["size"]){
                    $.alertWarning("视频尺寸或大小不合格");
                    return;
                }
                $("#adcreativeImgurl").val("");
                $("#adcreativeImageId").val("");
                $("."+uploadbtn).show();
                $("#adcreative_video").hide();
            });
        }

    });
}
function saveAdCreative(){
    var params = {};
    var imageUrl = $("#adcreativeImgurl").val();
    var image = $("#adcreativeImageId").val();
    if(image == ""){
        $.alertWarning("请上传新素材");
        return false;
    }
    params["id"] = $("#adcreativeId").val();
    params["img"] = image;
    params["imageUrl"] = imageUrl;
    params["adcreativeType"] = adcreative_type;
    Swal.fire({
        title: '确定更新所选的广告创意?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#EF5350",
        confirmButtonText: "是的, 确定",
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.value) {
            $.ajax({url:"/smartad-web/admin/ad/admanager/ad/updatecreative/"+aId,
                data:params,
                type:"POST",
                success:function(data){
                    if (data.status == 1) {
                        $.alertSuccess();
                    }else{
                        $.alertWarning(data.msg);
                    }
                },
                error:function(xhr, status){
                    $.alertWarning('失败: ' + xhr.status + ', 原因: ' + status);
                }
            });
        }
    });
}

