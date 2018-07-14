<template>
  <div >
    <div style="height: 40px;text-align: center;margin-top: 20px;">
      <span style="color: coral; font-size: 20px">NJU </span>
      <span style="color: slategray; font-size: 20px;">bazinga turk</span>
    </div>
    <div class="signBox">
      <p style="margin-left: 40px;font-size: 20px;color: dimgray;height: 20px">重置密码</p>

      <el-form :model="input"  :rules="signRule" ref="input"
               label-position="top" size="mini"
               style="margin-left: 40px;margin-right: 40px">
        <el-form-item label="电子邮箱地址" prop="email" >
          <el-input type="email" v-model="input.email" auto-complete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass" >
          <el-input type="password" v-model="input.pass" auto-complete="off" ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="passAgain" >
          <el-input type="password" v-model="input.passAgain" auto-complete="off" ></el-input>
        </el-form-item>
      </el-form>

      <el-button size="small" class="signbutton" style="margin-top: 30px" @click="confirm('input')" type="primary">确认</el-button>

    </div>
  </div>
</template>

<script>
    export default {
        name: "reset-pass-page",
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
          if(value!==this.input.pass){
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
          input:{
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


          signInInfo:{
            username:'',
            password:'',
          }
        }
      },

      created(){
        let url=window.location.href

        let email=''
        for(let i=0;i<url.length;i++){
          if(url[i]==='?'){
            email=url.substring(i+1)
            break
          }
        }

        let temp=new RegExp('%40','')
        email=email.replace(temp,'@')

        this.input.email=email
      },

      methods: {

        /**
         * 注册按钮的响应方法
         * @param formName 注册信息表单
         */
        confirm: function (formName) {

          console.log(sessionStorage.getItem('resetPassUser'))

          //判断输入是否合法
          this.$refs[formName].validate((valid) => {
            if (valid) {

              let _this = this

              _this.signInInfo.username = this.$refs[formName].model.email
              _this.signInInfo.password = this.$refs[formName].model.pass

              this.$api.post('/user/resetPassword', _this.signInInfo,r=>{
                if(r.data!=='FAIL'){
                  this.$message.success('密码修改成功')
                  this.$router.push('/')
                }
                else{
                  this.$message.error('该用户未注册或未激活！')
                }
              })

            } else {
              console.log('error submit!!');
              return false;
            }
          });

        },
      }
      }
</script>

<style scoped>
  .signBox{
    margin: auto;
    width: 300px;
    height: 450px;
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
