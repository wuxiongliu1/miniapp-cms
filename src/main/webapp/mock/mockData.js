var Random = Mock.Random;

Mock.mock('deleteImage.json', {
    'resultCode'     : 200,
    'resultMsg': '图片删除成功'
});

var src = Random.image('480x270');

Mock.mock('uploadImage.json', {
    'resultCode'     : 200,
    'resultMsg': '图片上传成功',
    "data": {
        "src": src
    }
});