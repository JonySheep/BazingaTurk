<template>
    <div>
      <div class="background">
        <p class="mainIntro">
          从这里开始你的众包数据之旅
        </p>
        <p class="detailIntro">
          Human intelligence through an API
        </p>
        <p class="detailIntro">
          Access a global,on-demand,24*7 workforce
        </p>
        <div class="beginbutton">
          <el-button type="danger" style="width: 120px">开始</el-button>
          <el-button type="info" style="width: 120px">了解更多</el-button>
        </div>
      </div>
      <div style="margin:50px;font-size: 15px">
        <p>
          Amazon Mechanical Turk (MTurk) operates a marketplace for work that requires human intelligence. The MTurk web service enables companies to programmatically access this marketplace and a diverse, on-demand workforce. Developers can leverage this service to build human intelligence directly into their applications.
        </p>
        <p>
          While computing technology continues to improve, there are still many things that human beings can do much more effectively than computers, such as identifying objects in a photo or video, performing data de-duplication, transcribing audio recordings or researching data details. Traditionally, tasks like this have been accomplished by hiring a large temporary workforce (which is time consuming, expensive and difficult to scale) or have gone undone.
        </p>
        <p>
          MTurk aims to make accessing human intelligence simple, scalable, and cost-effective. Businesses or developers needing tasks done (called Human Intelligence Tasks or “HITs”) can use the robust MTurk API to access thousands of high quality, global, on-demand Workers—and then programmatically integrate the results of that work directly into their business processes and systems. MTurk enables developers and businesses to achieve their goals more quickly and at a lower cost than was previously possible.
        </p>
      </div>
      <div>

        <el-row>
          <el-col :span="10">
            <div class="pic1"></div>
          </el-col>

          <el-col :span="14">
            <div style="margin-left: 100px">
              <el-row>
                <el-col :span="3">
                  <img src="../assets/icon/身份证.png" style="height: 55px;margin-top: 40px">
                </el-col>
                <el-col :span="14">
                  <div >
                    <p style="font-size: 25px;margin-bottom: 15px;color: dimgray ">众包发起者 & 众包工人</p>
                    <p style="margin-top:0px">BazingaTurk提供两种登录模式，你可以作为众包发起者或是众包工人登录系统来进行工作</p>
                  </div>
                </el-col>
              </el-row>
            </div>

            <div style="margin-left: 100px">
              <el-row>
                <el-col :span="3">
                  <img src="../assets/icon/任务.png" style="height: 60px;margin-top: 40px">
                </el-col>
                <el-col :span="14">
                  <div >
                    <p style="font-size: 25px;margin-bottom: 15px;color: dimgray">任务中心</p>
                    <p style="margin-top:0px">众包发起者可以在任务中心发布任务，供众包工人选择任务进行工作</p>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>
      <div>
        <el-row>
          <el-col :span="14">
            <div style="margin-left: 100px">
              <el-row>
                <el-col :span="3">
                  <img src="../assets/icon/礼物.png" style="height: 50px;margin-top: 60px">
                </el-col>
                <el-col :span="14">
                  <div >
                    <p style="font-size: 25px;margin-bottom: 15px;margin-top: 40px;color: dimgray">费用&回报</p>
                    <p style="margin-top:0px">众包工人完成任务可获得相应积分回报，积分可用于兑换工作费用</p>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div style="margin-left: 100px">
              <el-row>
                <el-col :span="3">
                  <img src="../assets/icon/统计.png" style="height: 50px;margin-top: 60px">
                </el-col>
                <el-col :span="14">
                  <div >
                    <p style="font-size: 25px;margin-bottom: 15px;margin-top: 40px;color: dimgray">可视化数据统计</p>
                    <p style="margin-top:0px">对任务工作的精确统计，最大化工作效率</p>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <el-col :span="10">
            <div class="pic2"></div>
          </el-col>
        </el-row>
      </div>
    </div>
</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
      components: {ElButton},
      name: "home-pic"
    }
</script>

<style scoped>
.background{
  height: 500px;
  background-image: url("../assets/homeBackground.jpg");
  background-size: cover;
}
  .mainIntro{
    font-size: 30px;
    color: white;
    padding-top: 150px;
    padding-left: 50px;
    margin:0px
  }
  .detailIntro{
    font-size: 18px;
    color: white;
    padding-left: 50px;
  }
  .beginbutton{
    padding-left: 50px;
    padding-top: 30px;
  }
  .pic1{
    background-image: url("../assets/data.jpg");
    background-size: cover;
    height: 300px;
  }
  .pic2{
    background-image: url("../assets/AI.jpg");
    background-size: cover;
    height: 300px;
  }
</style>
