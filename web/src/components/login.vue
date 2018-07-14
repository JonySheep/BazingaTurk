<template>
  <div >
    <el-alert
      v-if="errorInput"
      title="账号或密码错误"
      type="error"
      show-icon>
    </el-alert>
    <div style="height: 40px;text-align: center;margin-top: 20px;">
      <span style="color: coral; font-size: 20px">NJU </span>
      <span style="color: slategray; font-size: 20px;">bazinga turk</span>
    </div>
    <div class="loginBox">
      <p style="margin-left: 40px;font-size: 20px;color: dimgray;height: 20px">{{showRole}}登录</p>

      <el-form :model="loginInput"  :rules="loginRule" ref="loginInput"
               class="inputBox" label-position="top" size="mini"
               style="margin-left: 40px;margin-right: 40px">
        <el-form-item label="电子邮箱地址" prop="username">
          <el-input v-model="loginInput.username" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" >
          <el-input type="password" v-model="loginInput.password" auto-complete="off" ></el-input>
        </el-form-item>
      </el-form>

      <el-button size="small" class="loginbutton" style="margin-top: 25px" @click="onLogin('loginInput')">登录</el-button>
      <p v-if="role!=='Admin'"
        style="color: darkgray; font-size: 10px; text-align: center">——————没有账户？——————</p>
      <el-button
        size="small"
        class="loginbutton"
        @click="onSignIn"
        v-if="role!=='Admin'">注册</el-button>
      <el-button
        type="text"
        size="small"
        @click="findPass"
        style="margin-left: 120px;margin-top: 8px">忘记密码？</el-button>
      <p v-if="role==='Admin'"
         style="color: darkgray; font-size: 10px; text-align: center">此用户仅限后台管理人员进行登录</p>
    </div>
  </div>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: "login-page",
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
         * 检查输入密码的方法
         * @param rule 检验方法
         * @param value 输入值
         * @param callback 返回提示
         * @returns {*}
         */
        var checkPass=(rule,value,callback) => {


          if(value==='') {
            return callback(new Error('请输入密码'))
          }
          else{
            callback()
          }
        }

          return{
            loginInput:{
              username:'',
              password:'',
              role:''
            },

            loginRule:{
              username:[
                {validator:checkEmail,trigger:'blur'}
              ],
              password:[
                {validator:checkPass,trigger:'blur'}
              ]
            },

            showRole:"",  //用于界面显示的角色文字
            errorInput:false,
            user:{
              userId:''
            }
          }
      },

      props:['role'],

      created(){
        this.loginInput.role=this.role

        if(this.role==='Requester'){
          this.showRole="发布者"
        }
        else if(this.role==='Worker'){
          this.showRole="工作者"
        }
        else{
          this.showRole="系统管理员"
        }
      },

      methods:{

        /**
         * 点击登录按钮的响应方法
         * @param formName 登录信息表单
         * @constructor
         */
        onLogin:function (formName) {


          //判断输入信息是否合法
          this.$refs[formName].validate((valid) => {
            if (valid) {
              let _this = this
              let form=new FormData()
              form.append("username",this.loginInput.username)
              form.append("password",this.loginInput.password)

              this.$api.post('/login', form,r=>{
                if(r.success){

                  //若登录成功且账号密码正确
                  if(r.data!=='fail'){

                    let currentuser=''
                    this.$api.get('/user/current',null,res=>{
                      currentuser = res.data
                      if(_this.loginInput.role !== 'Admin'){

                        //sessionStorage store userInfo
                        sessionStorage.setItem("userID",currentuser)
                        _this.storeUserInfo()

                      }
                      else{
                        _this.$router.push('/adminPage')
                      }
                    })
                  }
                  else{
                    this.$message.error('账号或密码错误！')
                  }
                }

                else{
                  this.$message.error('服务器出了一些小问题～登录失败咯')
                }
              })
            }
            else {
              return false;
            }
          });
        },


        findPass:function () {

          this.$router.push('/resetPassEmail')
        },


        /**
         * 注册按钮的响应方法
         * 跳转至注册界面
         */
        onSignIn:function () {
          this.$router.push('/'+this.role.toLocaleLowerCase()+'SignIn')
        },


        storeUserInfo:function () {
          let _this=this
          this.$api.get('/user/'+sessionStorage.getItem("userID"), null, r => {

            //判断用户角色与当前角色是否相符
            if(r.data.role===this.loginInput.role){
              sessionStorage.setItem("name",r.data.name)
              sessionStorage.setItem("avatar",r.data.avatar)
              sessionStorage.setItem("role",r.data.role)

              this.$router.push({name:'taskCenter'})
            }
            else{
              this.$message.error('账号或密码错误！')

              //登出
              this.$api.get('/logout')
            }

          })
        }
      }
    }
</script>

<style scoped>
  .loginBox{
    margin: auto;
    width: 300px;
    height: 420px;
    border: darkgray 1px solid ;
    border-radius: 20px;
  }
  .loginbutton{
    width: 220px;
    height: 30px;
    margin-top: 5px;
    margin-left: 40px;
    float:none;
  }
</style>
