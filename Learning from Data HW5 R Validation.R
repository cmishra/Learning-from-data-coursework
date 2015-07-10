setwd("../Dropbox/College/3_Third Year/Summer Learning/Learning From Data/")
library(ggplot2)
library(data.table)

#Num 8 -- Making sure points are linearally separable
randDat <- data.table(read.csv("hw5Programs/test.csv", header=F))
ggplot(dat=randDat, aes(x=V2, y=V3, col=as.factor(V4))) +
  geom_point()

#Num 8 -- Make sure weights trained correctly 
logisticEval <- function(x) 1/(1+exp(-x))
setnames(randDat, names(randDat), c("x0", "x1", "x2", "actual", "pred"))

w <- read.csv("hw5Programs/testW.csv", header=F)[,1]
randDat[,"s":=as.matrix(randDat[,1:3, with=F])%*%w]
randDat[,"prob":=logisticEval(s)]
randDat[,"pred":=ifelse(prob>0.5, 1, -1)]
randDat[, "predWrong":=pred!=actual]

ggplot(dat=randDat, aes(x=x1, y=x2, col=as.factor(actual), 
                        size=as.factor(predWrong))) +
  geom_point()

randDat[predWrong == "TRUE"]
