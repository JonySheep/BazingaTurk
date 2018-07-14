<template>
  <div>
    <div style="height: 40px;text-align: center;margin-top: 20px;">
      <span style="color: coral; font-size: 20px">NJU </span>
      <span style="color: slategray; font-size: 20px;">bazinga turk</span>
    </div>
    <div class="Box">
      <p style="margin-top: 30px;font-size: 23px;">重置密码</p>

      <el-form :model="user"  :rules="emailRule" ref="user"
               class="inputBox" label-position="top" size="mini"
               style="margin-left: 40px;margin-right: 40px">
        <el-form-item label="电子邮箱地址" prop="username">
          <el-input v-model="user.username" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>

      <el-button style="" type="primary" size="small" @click="findPass('user')">确认</el-button>

      <p>
        <span style="font-size: 13px">感谢您选择</span>
        <span style="color: coral;font-size: 20px">bazingaTurk</span>
      </p>
      <p style="font-size: 13px">我们将会给您的注册邮箱发送一个链接，点击链接来进行密码重置</p>

      <el-button style="margin-top:20px;"  size="small" @click="toHome">回到首页</el-button>
    </div>
  </div>
</template>

<script>
    export default {
        name: "reset-pass-email-page",
      data() {

        /**
         * 检查email输入的方法
         * @param rule 检验方法
         * @param value 输入值
         * @param callback 返回提示
         * @returns {*}
         */
        var checkEmail = (rule, value, callback) => {

          //判断邮箱正确格式的正确格式
          let emailFilter = /^[A-Za-z0-9\u4e00-\u9fa5_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/

          if (value === '') {
            return callback(new Error('请输入您的邮箱'))
          }

          if (!emailFilter.test(value)) {
            return callback(new Error('邮箱格式错误'))
          }
          else {
            callback()
          }

        }

        return{
          user:{
            username:'',
          },
          emailRule:{
            username:[
              {validator:checkEmail,trigger:'blur'}
            ],
          }
        }
      },


      methods:{
        findPass:function (formName) {

          let form=new FormData()
          form.append("username",this.user.username)

          //判断输入信息是否合法
          this.$refs[formName].validate((valid) => {
            let _this=this
            if (valid) {

              console.log(form)

              this.$api.get('/user/retrievePassword/'+_this.user.username,null,r=>{
                if(r.success){
                  sessionStorage.setItem('resetPassUser',_this.user.username)
                  this.$message.success('已向您的账户中发送邮件，请注意查收～')
                }
                else{
                  this.$message.error('发送了一些小错误_(:з」∠)_请稍后再试')
                }
              })
            }
            else {
              console.log('error submit!!');

              return false;
            }
          });
        },

        toHome:function () {
          this.$router.push('/')
        }
      }

    }
</script>

<style scoped>
  .Box{
    margin: auto;
    width: 500px;
    height: 380px;
    border: darkgray 1px solid ;
    border-radius: 20px;
    text-align: center;
  }
</style>
