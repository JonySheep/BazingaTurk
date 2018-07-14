<template>
  <div style="margin-left: 200px;margin-right: 200px">
    <el-row>
      <el-col :span="5" style="margin-top: 20px">
        <p style="font-size: 20px">头像</p>
      </el-col>
      <el-col :span="6">

        <img v-bind:src="currentUserInfo.avatar" class="avatar">

      </el-col>

      <el-col :span="8" style="margin-top: 20px;text-align: left">
        <p style="margin-bottom: 30px">上传一个新的头像</p>
        <el-upload
          action=""
          class="upload-demo"
          :http-request="postAvatar"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          multiple
          :limit="1"
          :on-exceed="handleExceed"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传头像</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>

      </el-col>
    </el-row>

    <div class="line"></div>

    <el-row>
      <el-col :span="5" style="margin-top: 20px">
        <p style="font-size: 20px">信息</p>
      </el-col>
      <el-col :span="19" style="margin-top: 40px">
        <el-form :model="userInfo"
                 ref="userInfo"
                 status-icon :rules="rules"
                 label-width="100px"
                 label-position="top"
                 size="small"
                 class="demo-ruleForm">
          <el-form-item label="昵称" prop="name" >
            <el-input v-model="userInfo.name"
                      :placeholder=currentUserInfo.name
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="公开的邮箱" prop="public_email">
            <el-input v-model="userInfo.public_email"
                      :placeholder=currentUserInfo.publicEmail
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small"
                       class="updateButton"
                       @click="submitInfo('userInfo')">提交</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "user-info-page",

      data(){

        /**
         * 检查email输入的方法
         * @param rule 检验方法
         * @param value 输入值
         * @param callback 返回提示
         * @returns {*}
         */
        var checkEmail=(rule,value,callback) => {

          //判断邮箱正确格式的正确格式
          let emailFilter=/^[A-Za-z0-9\u4e00-\u9fa5_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/

          if(!emailFilter.test(value)&&value!==''){
            return callback(new Error('邮箱格式错误'))
          }
          else{
            callback()
          }

        }


        var checkName=(rule,value,callback) => {

          if(value===''){
            return callback(new Error('昵称不能为空'))
          }
          else{
            callback()
          }

        }

          return{

            user:{
              userID:''
            },

            avatarUrl:'http://localhost:3000/assets/img/jobs.e4f30b9.jpg',
            fileList:[],
            postAddress:'',
            userInfo:{
              name:'',
              public_email:'',
            },

            currentUserInfo:{},

            rules:{
              name:[
                {validator:checkName,trigger:'blur'}
              ],
              public_email:[
                {validator:checkEmail,trigger:'blur'}
              ]
            }
          }
      },

      mounted(){
        this.user.userId=sessionStorage.getItem("userID")
        this.setUserInfo()
      },

      methods:{

        /**
         * 移除头像的响应方法
         */
        handleRemove(file, fileList) {
          console.log(file, fileList);
        },

        /**
         * 上传成功的响应方法
         */
        handlePreview(file) {
          console.log(file);
          this.currentUserInfo.avatar=file.url
        },


        handleExceed(files, fileList) {
          this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },

        /**
         * 取消上传的响应方法
         */
        beforeRemove(file, fileList) {
          return this.$confirm(`确定移除 ${ file.name }？`);
        },


        /**
         * post头像文件的方法
         */
        postAvatar:function (item) {
          let formData=new FormData()

          formData.append('avatar',item.file)

          let _this=this

          //post
          this.$api.post('/user/'+sessionStorage.getItem("userID")+'/avatar',formData,r=>{
            _this.setUserInfo()
            window.location.reload()
          })


        },


        /**
         * 提交资料的方法
         * @param form
         */
        submitInfo:function (formName) {

          //判断输入信息是否合法
          this.$refs[formName].validate((valid) => {
            if (valid) {
              let _this = this


              let form =new FormData()
              form.append('public_email',_this.userInfo.public_email)
              form.append('name',_this.userInfo.name)

              sessionStorage.setItem('name',_this.userInfo.name)

              //post data
              this.$api.post('/user/'+sessionStorage.getItem("userID"),
                form, r=>{
                  //reload
                  window.location.reload()
                })
            }
            else {
              console.log('error submit!!');
              return false;
            }
          });
        },


        /**
         * 显示用户信息
         * @param userId
         */
        setUserInfo: function () {

          var _this = this
          this.$api.get('/user/'+sessionStorage.getItem("userID"), null, r => {
            _this.currentUserInfo = r.data
            sessionStorage.setItem("avatar",r.data.avatar)
          })

        }
      }
    }
</script>

<style scoped>

  .avatar {
    margin-top: 40px;
    border: lightgrey 1px solid;
    width: 150px;
    height: 150px;
    display: block;
  }

  .line{
    margin-top: 30px;
    border-bottom: darkgray 1px solid;
  }

  .updateButton{
    margin-top: 10px;
    margin-right: 10px;
    background-color: #38AE67 ;
    height: 30px;
    width: 80px;
    color:white;
  }
</style>
