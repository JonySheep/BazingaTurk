<template>
  <div style="margin-left: 200px;margin-right: 200px">
    <el-row>
      <el-col :span="5" style="margin-top: 20px">
        <p style="font-size: 20px">账户</p>
      </el-col>

      <el-col :span="19" style="margin-top: 40px">
        <el-form :model="userAccountEmail" :rules="emailRule" ref="userAccountEmail" status-icon>
          <el-form-item label="邮箱" prop="newEmail">
            <el-input v-model="userAccountEmail.newEmail"
                      :placeholder=userInfo.username
                      size="small"
                      clearable
                      auto-complete="off"></el-input>
          </el-form-item>
        </el-form>

        <el-button size="small"
                   class="updateButton"
                   @click="submitUsername('userAccountEmail')">提交</el-button>
      </el-col>
    </el-row>

    <div class="line"></div>

    <el-row>
      <el-col :span="5" style="margin-top: 20px">
        <p style="font-size: 20px">密码</p>
      </el-col>

      <el-col :span="19" style="margin-top: 40px">

        <el-form :model="password" :rules="passRules" ref="password" status-icon>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="password.newPassword"
                      type="password"
                      placeholder="请输入新密码"
                      size="small"
                      clearable
                      auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="确认新密码" prop="newPasswordAgain">
            <el-input v-model="password.newPasswordAgain"
                      type="password"
                      placeholder="请再次输入新密码"
                      size="small"
                      clearable
                      auto-complete="off"></el-input>
          </el-form-item>
        </el-form>

        <el-button size="small"
                   class="updateButton"
                   @click="submitPass('password')">提交</el-button>
      </el-col>
    </el-row>

    <div class="line"></div>

    <el-row>
      <el-col :span="5" style="margin-top: 20px">
        <p style="font-size: 20px">移除</p>
      </el-col>

      <el-col :span="19" style="margin-top: 40px">
        <el-button
          size="small"
          @click="removeUser"
          class="removeButton">移除该账户</el-button>
      </el-col>
    </el-row>

  </div>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: "user-acount-page",

      data(){

        var checkEmail=(rule,value,callback) => {

          //判断邮箱正确格式的正确格式
          let emailFilter=/^[A-Za-z0-9\u4e00-\u9fa5_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/

          if(value===''){
            return callback(new Error('请输入您的邮箱'))
          }

          if(!emailFilter.test(value)){
            return callback(new Error('邮箱格式错误'))
          }
          else{
            callback()
          }

        }

        /**
         * 检查设置密码的方法
         * 必须以大写字母开头，且必须含有小写字母和数字，长度为6-18位
         * @param rule 检验方法
         * @param value 输入值
         * @param callback 返回提示
         * @returns {*}
         */
        var checkPass=(rule,value,callback) => {
          var passFilter=/^[A-Z]+(?![0-9]+$)(?![a-zA-Z]+$)(?![A-Z0-9]+$)[0-9A-Za-z]{5,18}$/

          if(value===''){
            return callback(new Error('请输入密码'))
          }

          if(!passFilter.test(value)){
            return callback(new Error('密码格式不正确！必须以大写字母开头，含有小写字母和数字，长度为6-18位'))
          }
          else{
            callback()
          }
        }


        /**
         * 确认两次密码是否一致的方法
         * @param rule 检查方法
         * @param value 输入值
         * @param callback 返回提示
         * @returns {*}
         */
        var checkPassAgain=(rule,value,callback) => {
          if(value===''){
            return callback(new Error('请输入密码'))
          }

          if(value!==this.password.newPassword){
            return callback(new Error('两次输入密码不一致'))
          }
          else{
            callback()
          }
        }

        return{

          userInfo:{},

          userAccountEmail:{
            newEmail:''
          },
          password:{
            newPassword:'',
            newPasswordAgain:'',
          },

          emailRule:{
            newEmail:[
              {validator:checkEmail,trigger:'blur'}
            ]
          },

          passRules:{
            newPassword:[
              {validator:checkPass,trigger:'blur'}
            ],
            newPasswordAgain:[
              {validator:checkPassAgain,trigger:'blur'}
            ]

          }
        }
      },

      mounted(){
        this.setUserInfo()
      },

      methods:{
        /**
         * 提交资料的方法
         * @param form
         */
        submitUsername:function (formName) {
          //判断输入信息是否合法

          this.$refs[formName].validate((valid) => {
            if (valid) {
              let _this = this

              let form =new FormData()
              form.append('email',_this.userAccountEmail.newEmail)

              //post data
              this.$api.post('/user/'+sessionStorage.getItem('userID')+'/accountEmail',
                form,r=>{
                  //reload page
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
           * 提交新密码的方法
           * @param form
           */
          submitPass:function (formName) {
            //判断输入信息是否合法

            this.$refs[formName].validate((valid) => {
              if (valid) {
                let _this = this

                let form =new FormData()
                form.append('password',_this.password.newPassword)


                //post data
                this.$api.post('/user/'+sessionStorage.getItem('userID')+'/password',
                  form,r=>{
                    //reload page
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
            _this.userInfo = r.data
          })
        },


        /**
        移除当前用户，移除后跳转到首页
         */
        removeUser:function () {

          var _this=this

          this.$confirm('此操作将永久移除该用户, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            this.$api.get('/user/'+sessionStorage.getItem("userID")+'/remove', null, r => {
              _this.$router.push('/')
            })

            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });

        }
      }
    }
</script>

<style scoped>

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

  .removeButton{
    margin-right: 10px;
    background-color: #E52C5A ;
    height: 30px;
    width: 100px;
    color:white;
    text-align: center;
  }
</style>
