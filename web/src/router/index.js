import Vue from 'vue'
import Router from 'vue-router'

//分类标注界面
import PartClassification from '@/page/annotationPages/partClassification.vue'
import WholeClassification from '@/page/annotationPages/wholeClassification.vue'
import PartClassificationDoneCheck from '@/page/annotationPages/partClassificationDoneCheck.vue'

//检测标注界面
import Detection from '@/page/annotationPages/detection.vue'
import DetectionDoneCheck from '@/page/annotationPages/detectionDoneCheck.vue'

//区域划分界面
import SegmentPage from '@/page/annotationPages/segmentPage.vue'
import SegCheckPage from '@/page/annotationPages/segmentCheckPage.vue'

//个人任务界面
import TaskManage from '@/page/taskPages/taskManage.vue'
import RequesterTaskManage from '@/page/taskPages/requesterTask.vue'

//已完成任务界面
import TaskDone from '@/page/taskPages/taskDone.vue'
import RequesterTaskDone from '@/page/taskPages/requesterTaskDone.vue'

//主页
import Homepage from '@/page/utilPages/homePage.vue'

//登录界面
import WorkerLogin from '@/page/utilPages/loginPages/workerLoginPage.vue'
import ReqLogin from '@/page/utilPages/loginPages/requesterLoginPage.vue'
import resetPass from '@/page/utilPages/loginPages/resetPassPage.vue'
import resetPassFail from '@/page/utilPages/loginPages/resetPassFailPage.vue'
import resetPassEmail from '@/page/utilPages/loginPages/resetPassEmailPage.vue'

//系统管理者登录入口
import AdminLogin from '@/page/utilPages/loginPages/adminLoginPage.vue'

//注册
import WorkerSign from '@/page/utilPages/signInPages/workerSignPage.vue'
import ReqSign from '@/page/utilPages/signInPages/requesterSignPage.vue'
import signSuccess from '@/page/utilPages/signInPages/signSuccessPage.vue'
import signFail from '@/page/utilPages/signInPages/signFailPage.vue'
import Activation from '@/page/utilPages/signInPages/activationPromptPage.vue'

//任务大厅界面
import TaskLobby from '@/page/taskPages/taskLobby.vue'
import TaskDetail from '@/page/taskPages/taskDetail.vue'

//个人中心界面
import UserCenter from '@/page/userPages/userCenterPage.vue'
import UserInfo from '@/page/userPages/userInfoMainPage.vue'
import UserRank from '@/page/userPages/userRankPage.vue'

import TaskConfig from '@/page/taskRelease/taskConfig.vue'

//后台管理界面
import AdminPage from '@/page/adminPages/adminMainPage.vue'
import UserManagePage from '@/page/adminPages/userManagePage.vue'
import TaskManagePage from '@/page/adminPages/taskManagePage.vue'

//任务中心
import TaskCenter from '@/page/taskPages/taskMainPage.vue'

//发起者任务分组情况查看界面
import TaskPartition from '@/page/taskPages/taskPartitionList.vue'

Vue.use(Router)

export default new Router({
  routes: [

    //主页
    {
      path: '/',
      component: Homepage,
      meta:{
        title:'Bazinga Turk'
      }
    },

    //登录/注册
    {
      path:'/workerLogin',
      component:WorkerLogin,
      meta:{
        title:'工作者登录 | Bazinga Turk'
      }
    },
    {
      path:'/requesterLogin',
      component:ReqLogin,
      meta:{
        title:'发起者登录 | Bazinga Turk'
      }
    },
    {
      path:'/workerSignIn',
      component:WorkerSign,
      meta:{
        title:'工作者注册 | Bazinga Turk'
      }
    },
    {
      path:'/requesterSignIn',
      component:ReqSign,
      meta:{
        title:'发起者注册 | Bazinga Turk'
      }
    },
    {
      path:'/signUpSuccess',
      component:signSuccess,
    },
    {
      path:'/signFail',
      component:signFail,
    },
    {
      path:'/activation',
      component:Activation,
    },
    {
      path:'/resetPass',
      component:resetPass,
    },
    {
      path:'/resetPassFail',
      component:resetPassFail,
    },
    {
      path:'/resetPassEmail',
      component:resetPassEmail,
    },


    //系统管理员登录入口
    {
      path:'/admin',
      component:AdminLogin,
      meta:{
        title:'管理员登录 | Bazinga Turk'
      }
    },


    //工人个人任务
    {
      path:'/taskManage',
      name:'taskManage',
      component:TaskManage,
      meta:{
        title:'管理员登录 | Bazinga Turk'
      }
    },

    //发起者个人任务
    {
      path:'/requesterTaskManage',
      component:RequesterTaskManage,
      meta:{
        title:'任务中心 | Bazinga Turk'
      }
    },

    //工人个人已完成任务
    {
      path:'/taskDone',
      component:TaskDone,
      meta:{
        title:'任务中心 | Bazinga Turk'
      }
    },

    //发起者个人已完成任务
    {
      path:'/requesterTaskDone',
      component:RequesterTaskDone,
      meta:{
        title:'任务中心 | Bazinga Turk'
      }
    },
    //任务大厅
    {
      path:'/taskCenter',
      name:'taskCenter',
      component:TaskCenter,
      meta:{
        title:'任务中心 | Bazinga Turk'
      }
    },



    //区域划分
    {
      path:'/segment/:state/:taskId/:assignmentId/:workerId',
      component:SegmentPage,
      name:'segPage',
    },
    {
      path:'/segmentCheck/:taskId/:assignmentId/:workerId',
      component:SegCheckPage,
    },


    //分类标注
    {
      path:'/partClassification/:state/:taskId/:assignmentId/:workerId',
      component:PartClassification,
    },
    {
      path:'/wholeClassification/:id',
      component:WholeClassification,
    },
    {
      path:'/partClassificationDoneCheck/:taskId/:assignmentId/:workerId',
      component:PartClassificationDoneCheck
    },


    //检测标注
    {
      path:'/detection/:state/:taskId/:assignmentId/:workerId',
      component:Detection
    },
    {
      path:'/detectionDoneCheck/:taskId/:assignmentId/:workerId',
      component:DetectionDoneCheck
    },


    {
      path:'/taskLobby/:id',
      component:TaskDetail,
      meta:{
        title:'任务指引 | Bazinga Turk'
      }
    },
    {
      path:'/taskPartition/:taskId',
      component:TaskPartition,

    },


    //个人中心
    {
      path:'/userCenter',
      component:UserCenter,
      meta:{
        title:'个人中心 | Bazinga Turk'
      }
    },
    {
      path:'/userInfo',
      component:UserInfo,
      meta:{
        title:'个人资料 | Bazinga Turk'
      }
    },
    {
      path:'/userRank',
      component:UserRank,
      meta:{
        title:'个人排名 | Bazinga Turk'
      }
    },


    {
      path: '/upload',
      component: TaskConfig,
      meta:{
        title:'任务发布 | Bazinga Turk'
      }
    },

    //后台管理界面
    {
      path:'/adminPage',
      component:AdminPage,
      meta:{
        title:'系统管理 | Bazinga Turk'
      }
    },
    {
      path:'/userManagePage',
      component:UserManagePage,
      meta:{
        title:'系统管理 | Bazinga Turk'
      }
    },
    {
      path:'/taskManagePage',
      component:TaskManagePage,
      meta:{
        title:'系统管理 | Bazinga Turk'
      }
    }
  ]
})
