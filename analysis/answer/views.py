from django.http import HttpResponse
import math
import numpy as np
import scipy
from collections import defaultdict
import warnings

#数据格式
#获得boxes[],tags[]
#box内容{boxId,xpos,ypos,width,height,workerId}
#tag内容在此部分需要根据boxId增加clusterId
#返回一个accuracies[] {workerId,accuracy}
#合成一个boxlist[] {boxId, clusterId}

def index(request, task_id):
    tags = request.POST.get('tags')
    boxes = request.POST.get('boxes')
    accuracies = request.POST.get('accuracies')
    attributeSize = request.POST.get('attributeSize')
    attributeSize = int(attributeSize)

    #boxes = '[{"boxId":"4028638164125e7d01641267ffbc0050","xpos":271,"ypos":83,"width":167,"height":215,"workerId":"4028638164121096016412129eb00002"},{"boxId":"4028638164125e7d01641267ffc10055","xpos":424,"ypos":296,"width":137,"height":175,"workerId":"4028638164121096016412129eb00002"},{"boxId":"4028638164125e7d01641267ffc2005a","xpos":609,"ypos":203,"width":134,"height":189,"workerId":"4028638164121096016412129eb00002"},{"boxId":"4028638164125e7d01641267ffc2005f","xpos":816,"ypos":161,"width":145,"height":186,"workerId":"4028638164121096016412129eb00002"},{"boxId":"4028638164125e7d01641267ffc30064","xpos":1010,"ypos":63,"width":161,"height":201,"workerId":"4028638164121096016412129eb00002"},{"boxId":"4028638164125e7d0164126e60a400a4","xpos":263,"ypos":72,"width":178,"height":229,"workerId":"402863816412109601641214cc3e0008"},{"boxId":"4028638164125e7d0164126e60a400a9","xpos":429,"ypos":291,"width":134,"height":182,"workerId":"402863816412109601641214cc3e0008"},{"boxId":"4028638164125e7d0164126e60a500ae","xpos":596,"ypos":199,"width":143,"height":189,"workerId":"402863816412109601641214cc3e0008"},{"boxId":"4028638164125e7d0164126e60a500b3","xpos":817,"ypos":156,"width":152,"height":200,"workerId":"402863816412109601641214cc3e0008"},{"boxId":"4028638164125e7d0164126e60a600b8","xpos":1015,"ypos":64,"width":167,"height":200,"workerId":"402863816412109601641214cc3e0008"},{"boxId":"4028638164125e7d016412712ed800f4","xpos":247,"ypos":78,"width":200,"height":215,"workerId":"40286381641210960164121ed4b60010"},{"boxId":"4028638164125e7d016412712ed900f9","xpos":412,"ypos":298,"width":152,"height":168,"workerId":"40286381641210960164121ed4b60010"},{"boxId":"4028638164125e7d016412712ed900fe","xpos":605,"ypos":193,"width":142,"height":200,"workerId":"40286381641210960164121ed4b60010"},{"boxId":"4028638164125e7d016412712ed90103","xpos":826,"ypos":167,"width":140,"height":178,"workerId":"40286381641210960164121ed4b60010"},{"boxId":"4028638164125e7d016412712ed90108","xpos":1010,"ypos":47,"width":162,"height":221,"workerId":"40286381641210960164121ed4b60010"},{"boxId":"4028638164125e7d01641274a1050148","xpos":263,"ypos":68,"width":176,"height":218,"workerId":"402863816412109601641229a9b70014"},{"boxId":"4028638164125e7d01641274a105014d","xpos":435,"ypos":294,"width":130,"height":185,"workerId":"402863816412109601641229a9b70014"},{"boxId":"4028638164125e7d01641274a1060152","xpos":606,"ypos":187,"width":133,"height":202,"workerId":"402863816412109601641229a9b70014"},{"boxId":"4028638164125e7d01641274a1060157","xpos":810,"ypos":158,"width":171,"height":188,"workerId":"402863816412109601641229a9b70014"},{"boxId":"4028638164125e7d01641274a106015c","xpos":1015,"ypos":55,"width":167,"height":210,"workerId":"402863816412109601641229a9b70014"},{"boxId":"4028638164125e7d0164127a5bb60184","xpos":242,"ypos":76,"width":198,"height":227,"workerId":"40286381641210960164122c9f870018"},{"boxId":"4028638164125e7d0164127a5bb60189","xpos":425,"ypos":308,"width":126,"height":183,"workerId":"40286381641210960164122c9f870018"},{"boxId":"4028638164125e7d0164127a5bb6018e","xpos":604,"ypos":217,"width":143,"height":181,"workerId":"40286381641210960164122c9f870018"},{"boxId":"4028638164125e7d0164127a5bb70193","xpos":804,"ypos":186,"width":134,"height":168,"workerId":"40286381641210960164122c9f870018"},{"boxId":"4028638164125e7d0164127a5bb70198","xpos":994,"ypos":71,"width":191,"height":179,"workerId":"40286381641210960164122c9f870018"}]'
    #tags = '[{"boxId":"4028638164125e7d01641267ffbc0050","name":"19岁-25岁","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffbc0050","name":"黑色","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffbc0050","name":"男","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffbc0050","name":"白种人","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc10055","name":"19岁-25岁","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc10055","name":"黑色","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc10055","name":"男","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc10055","name":"白种人","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005a","name":"19岁-25岁","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005a","name":"棕色","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005a","name":"男","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005a","name":"白种人","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005f","name":"19岁-25岁","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005f","name":"棕色","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005f","name":"男","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc2005f","name":"白种人","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc30064","name":"26岁-45岁","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc30064","name":"黑色","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc30064","name":"男","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d01641267ffc30064","name":"黑种人","colorHex":"","workerId":"4028638164121096016412129eb00002","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a4","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a4","name":"黑色","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a4","name":"男","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a4","name":"白种人","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a9","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a9","name":"黑色","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a9","name":"男","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a400a9","name":"白种人","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500ae","name":"26岁-45岁","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500ae","name":"金色","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500ae","name":"男","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500ae","name":"白种人","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500b3","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500b3","name":"棕色","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500b3","name":"男","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a500b3","name":"白种人","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a600b8","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a600b8","name":"黑色","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a600b8","name":"男","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d0164126e60a600b8","name":"黑种人","colorHex":"","workerId":"402863816412109601641214cc3e0008","clusterId":-1},{"boxId":"4028638164125e7d016412712ed800f4","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed800f4","name":"黑色","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed800f4","name":"男","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed800f4","name":"白种人","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900f9","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900f9","name":"黑色","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900f9","name":"男","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900f9","name":"白种人","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900fe","name":"18岁以下","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900fe","name":"黑色","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900fe","name":"男","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed900fe","name":"白种人","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90103","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90103","name":"棕色","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90103","name":"男","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90103","name":"白种人","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90108","name":"18岁以下","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90108","name":"黑色","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90108","name":"男","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d016412712ed90108","name":"黑种人","colorHex":"","workerId":"40286381641210960164121ed4b60010","clusterId":-1},{"boxId":"4028638164125e7d01641274a1050148","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1050148","name":"黑色","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1050148","name":"男","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1050148","name":"白种人","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a105014d","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a105014d","name":"黑色","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a105014d","name":"男","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a105014d","name":"白种人","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060152","name":"19岁-25岁","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060152","name":"金色","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060152","name":"男","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060152","name":"白种人","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060157","name":"26岁-45岁","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060157","name":"黑色","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060157","name":"男","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a1060157","name":"白种人","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a106015c","name":"26岁-45岁","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a106015c","name":"黑色","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a106015c","name":"男","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d01641274a106015c","name":"黑种人","colorHex":"","workerId":"402863816412109601641229a9b70014","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60184","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60184","name":"黑色","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60184","name":"男","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60184","name":"白种人","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60189","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60189","name":"黑色","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60189","name":"男","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb60189","name":"不清楚","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb6018e","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb6018e","name":"棕色","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb6018e","name":"男","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb6018e","name":"白种人","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70193","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70193","name":"棕色","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70193","name":"男","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70193","name":"白种人","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70198","name":"19岁-25岁","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70198","name":"黑色","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70198","name":"男","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1},{"boxId":"4028638164125e7d0164127a5bb70198","name":"黑种人","colorHex":"","workerId":"40286381641210960164122c9f870018","clusterId":-1}]'
    #accuracies = '[{"accuracy":0.0,"workerId":"4028638164121096016412129eb00002"},{"accuracy":0.0,"workerId":"402863816412109601641214cc3e0008"},{"accuracy":0.0,"workerId":"40286381641210960164121ed4b60010"},{"accuracy":0.0,"workerId":"402863816412109601641229a9b70014"},{"accuracy":0.0,"workerId":"40286381641210960164122c9f870018"}]'

    boxes = eval(boxes)
    tags = eval(tags)
    accuracies = eval(accuracies)

    dataset = [(item["boxId"], item["xpos"], item["ypos"], item["xpos"]+item["width"], item["ypos"]+item["height"], item["workerId"]) for item in boxes]

    knn = np.transpose(k_dist(dataset))[1:]
    eps = calEps(knn)
    minPts = calMinPts(dataset,eps)

    clusters = DBSCAN(dataset,eps,minPts)
    finalres = integrateAnno(clusters)
    accuracytemp = calAccuracy(finalres) #临时变量，不用作为返回值
    accuracies = formAccuracyList(accuracytemp) #作为返回值，accuracies[]
    tagList = formTagList(accuracytemp,tags)
    boxesReturn = formBoxes(accuracytemp)
    tagsReturn = formTags(tagList,attributeSize)
    annoReturn = formAnno(boxesReturn,tagsReturn)
    return HttpResponse(str(annoReturn)+"||"+str(accuracies))

def k_dist(dataset):
    distarray = [[0 for i in range(len(dataset))] for i in range(len(dataset))]
    for i in range(0, len(dataset)):
        for j in range(0, len(dataset)):
            distarray[i][j] = dist(dataset[i],dataset[j])

    for i in range(0, len(dataset)):
        distarray[i].sort()

    return distarray

def dist(a,b):
    return math.sqrt(math.pow(a[1] - b[1], 2) + math.pow(a[2] - b[2], 2) + math.pow(a[3] - b[3], 2) + math.pow(a[4] - b[4], 2))

def calEps(knn):
    x = scipy.array(range(len(knn[0])*len(knn)))
    res = []
    for i in range(0, len(knn)):
        for j in range(0, len(knn[0])):
            res.append(knn[i][j])

    y = scipy.array(sorted(res))

    polynomial = np.polyfit(x,y,10)
    warnings.simplefilter('ignore',np.RankWarning)
    square1 = np.poly1d(polynomial)
    square2 = square1.deriv()
    square3 = square2.deriv()
    rootOf3 = np.roots(square3)

    candidate = []
    for root in rootOf3:
        if root>0 and root<=len(knn[0])*len(knn):
            candidate.append((square2(root),root))

    eps = square1(max(candidate)[1])
    return eps

def calMinPts(dataset,eps):
    neighbourNum = []
    for i in range(0,len(dataset)):
        neighbourNum.append(calNeighbourNum(i,eps,dataset))

    minPts = sum(neighbourNum)/len(neighbourNum)
    return minPts

def calNeighbourNum(Id, eps, dataset):
    num = 0
    for i in range(0, len(dataset)):
        if i!=Id and dist(dataset[Id],dataset[i])<=eps:
            num = num+1
    return num

def DBSCAN(D, e, Minpts):
    #初始化核心对象集合T,聚类个数k,聚类集合C, 未访问集合P,
    T = set(); k = 0; C = []; P = set(D)
    for d in D:
        if len([ i for i in D if dist(d, i) <= e]) >= Minpts:
            T.add(d)
    #开始聚类
    while len(T):
        P_old = P
        o = list(T)[np.random.randint(0, len(T))]
        P = P - set(o)
        Q = []; Q.append(o)
        while len(Q):
            q = Q[0]
            Nq = [i for i in D if dist(q, i) <= e]
            if len(Nq) >= Minpts:
                S = P & set(Nq)
                Q += (list(S))
                P = P - S
            Q.remove(q)
        k += 1
        Ck = list(P_old - P)
        T = T - set(Ck)
        C.append(Ck)
    return C

def integrateAnno(clusters):
    result = []
    i = 0
    for cluster in clusters:
        left = top = right = bottom =0
        clustertemp = []
        for item in cluster:
            left = left+item[1]
            top = top+item[2]
            right = right+item[3]
            bottom = bottom+item[4]
            itemafter = item+(i,)
            clustertemp.append(itemafter)

        left = round(left/len(cluster))
        top = round(top/len(cluster))
        right = round(right/len(cluster))
        bottom = round(bottom/len(cluster))
        result.append(((left, top, right, bottom, i),clustertemp))
        i = i+1
    return result

def calAccuracy(finalres):
    result = []
    for part in finalres:
        finalresTemp = []
        standard = part[0]
        for answer in part[1]:
            atoi = calAtoi(standard,answer)
            temp = answer+(atoi,)
            finalresTemp.append(temp)
        result.append((standard,finalresTemp))
    return result

def calAtoi(standard,answer):
    width = min(standard[2],answer[3])-max(standard[0],answer[1])
    height = min(standard[3],answer[4])-max(standard[1],answer[2])
    if width<=0 or height<=0:
        return 0
    Sstandard = (standard[2]-standard[0])*(standard[3]-standard[1])
    Sanswer = (answer[3]-answer[1])*(answer[4]-answer[2])
    cross = width*height
    atoi = cross/(Sstandard+Sanswer-cross)
    atoi = math.log(atoi+1,2)
    return atoi

def formAccuracyList(accuracyTemp):
    accuraciesdic = defaultdict(list)
    for cluster in accuracyTemp:
        for item in cluster[1]:
            accuraciesdic[item[5]].append(item[7])

    accuracies = []
    for key in accuraciesdic:
        temp ={}
        temp["workerId"] = key
        temp["accuracy"] = sum(accuraciesdic[key])/len(accuraciesdic[key])
        accuracies.append(temp)

    return accuracies

def formTagList(accuracyTemp,tagList):
    boxlist = {}
    for cluster in accuracyTemp:
        for item in cluster[1]:
            boxlist[item[0]] = item[6]

    for tag in tagList:
        if tag["boxId"] in boxlist:
            tag["clusterId"] = boxlist[tag["boxId"]]

    return tagList

def formBoxes(accuracyTemp):
    boxes = []
    for cluster in accuracyTemp:
        standard = cluster[0]
        temp = {"boxId":standard[4],"xpos":standard[0],"ypos":standard[1],"width":standard[2]-standard[0],"height":standard[3]-standard[1],"workerId":""}
        boxes.append(temp)
    return boxes

def formTags(tagList,attributeSize):
    tags = defaultdict(list)
    tagsTemp = []
    for i in range(0,len(tagList),attributeSize):
        temp = []
        for j in range(attributeSize):
            temp.append(tagList[i+j])
        tagsTemp.append(temp)
    boxTags = defaultdict(list)
    i = 0
    for tag in range(0, len(tagsTemp)):
        boxTags[tagsTemp[i][0]["clusterId"]].append(tagsTemp[i])
        i = i+1
    for box in boxTags:
        res = []
        for count in range(attributeSize):
            nameTemp = {}
            for tag in boxTags[box]:
                nameTemp.setdefault(tag[count]["name"], 0)
                nameTemp[tag[count]["name"]] = nameTemp[tag[count]["name"]]+1
            nameRes = {"id":"","name":max(nameTemp,key=nameTemp.get), "colorHex":""}
            res.append(nameRes)
        tags[box].append(res)

    return tags

def formAnno(boxesReturn, tagsReturn):
    anno = {}
    anno["id"] = ""
    anno["imageUrl"] = ""
    anno["assignmentId"] = ""
    anno["workerId"] = ""
    anno["imageId"] = 0
    boxes = []
    for box in boxesReturn:
        temp = {}
        temp["id"] = ""
        temp["xpos"] = box["xpos"]
        temp["ypos"] = box["ypos"]
        temp["width"] = box["width"]
        temp["height"] = box["height"]
        temp["tags"] = tagsReturn[box["boxId"]][0]
        temp["colorHex"] = ""
        boxes.append(temp)
    anno["boxes"] = boxes

    return anno