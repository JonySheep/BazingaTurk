<template>
  <div >
    <div style="height: 40px;text-align: center;margin-top: 20px;">
      <span style="color: coral; font-size: 20px">NJU </span>
      <span style="color: slategray; font-size: 20px;">bazinga turk</span>
    </div>
    <div class="signBox">
      <p style="margin-left: 40px;font-size: 20px;color: dimgray;height: 20px">{{showRole}}注册</p>

      <el-form :model="signInput"  :rules="signRule" ref="signInput"
                label-position="top" size="mini"
               style="margin-left: 40px;margin-right: 40px">
        <el-form-item label="电子邮箱地址" prop="email">
          <el-input type="email" v-model="signInput.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass" >
          <el-input type="password" v-model="signInput.pass" auto-complete="off" ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="passAgain" >
          <el-input type="password" v-model="signInput.passAgain" auto-complete="off" ></el-input>
        </el-form-item>
      </el-form>

      <el-button size="small" class="signbutton" style="margin-top: 25px" @click="onSignIn('signInput')">注册</el-button>
      <p style="color: darkgray; font-size: 10px; text-align: center">——————已有账户？——————</p>
      <el-button size="small" class="signbutton" @click="onLogin()">登录</el-button>

    </div>
  </div>
</template>

<script>
    export default {
      name: "sign-in-page",
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

          //TODO 判断邮箱是否已被注册
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
          if(value!==this.signInput.pass){
            return callback(new Error('两次输入密码不一致'))
          }
          else{
            callback()
          }

          if(value===''){
            return callback(new Error('请输入密码'))
          }
          else{
            callback()
          }
        }


        return{
          signInput:{
            email:'',
            pass:'',
            passAgain:''
          },
          signRule:{
            email:[
              {validator:checkEmail,trigger:'blur'}
            ],
            pass:[
              {validator:checkPass,trigger:'blur'}
            ],
            passAgain:[
              {validator:checkPassAgain,trigger:'blur'}
            ],
          },

          showRole:'',

          signInInfo:{
            username:'',
            password:'',
            role:''
          }
        }
      },

      props:['role'],

      created(){

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
         * 注册按钮的响应方法
         * @param formName 注册信息表单
         */
        onSignIn:function (formName) {

          let _this = this
          //判断输入是否合法
          this.$refs[formName].validate((valid) => {
            if (valid) {

              _this.signInInfo.username=this.$refs[formName].model.email
              _this.signInInfo.password=this.$refs[formName].model.pass
              _this.signInInfo.role=this.role


              console.log(_this.signInInfo)

              this.$api.post('/signUp', _this.signInInfo,r=>{

                //若该账号已被注册
                if(r.data==='FAIL'){
                  this.$message.error('该邮箱已被注册！:-P换一个吧～')
                  this.clearInfo()
                }
                else{
                  this.$router.push('/activation')
                }

                if(!r.success){
                  this.$message.error('服务器出错啦～请稍后再尝试注册～')
                }
              })

            } else {
              console.log('error submit!!');
              return false;
            }
          });

        },


        /**
         * 登录按钮的响应方法
         * 点击后跳转至登录界面
         * @constructor
         */
        onLogin:function () {
          this.$router.push('/'+this.role.toLocaleLowerCase()+'login')
        },


        /**
         * 清空输入框的方法
         */
        clearInfo:function () {
          this.signInput.email=''
          this.signInput.pass=''
          this.signInput.passAgain=''
        }
      }
    }
</script>

<style scoped>

  .signBox{
    margin: auto;
    width: 300px;
    height: 480px;
    border: darkgray 1px solid ;
    border-radius: 20px;
  }
  .signbutton{
    width: 220px;
    height: 30px;
    margin: 5px;
    margin-left: 40px;
  }
</style>
