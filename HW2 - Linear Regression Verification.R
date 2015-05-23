## HW 2 linear regression test
library(data.table)

test <- data.table(x=runif(100, -1, 1), y=runif(100, -1, 1), 
  c1=sample(0:1, 100, replace=T))

point1 <- runif(2, -1, 1)
point2 <- runif(2, -1, 1)
m <- (point1[2]-point2[2])/(point1[1] - point2[1])
b <- point1[2]-point1[1]*m

test[,c2:=ifelse(y>b+m*x, 1, 0)]

lm1 <- lm(data=test, c1 ~ x + y)
lm2 <- lm(data=test, c2 ~ x + y)


mean(unlist(lapply(1:1000, function(i) {
  test <- data.table(x=runif(100, -1, 1), y=runif(100, -1, 1))
  
  point1 <- runif(2, -1, 1)
  point2 <- runif(2, -1, 1)
  m <- (point1[2]-point2[2])/(point1[1] - point2[1])
  b <- point1[2]-point1[1]*m
  
  test[,c2:=ifelse(y>b+m*x, 1, 0)]
  
  lm2 <- lm(data=test, c2 ~ x + y)
  summary(lm2)$sigma
})))


## Prob 9/10 Test 

weigths <- matrix(unlist(lapply(1:100, function(i) {
  test2 <- data.table(x=runif(100, -1, 1), y=runif(100, -1, 1))
  test2[,x2:=x^2]
  test2[,y2:=y^2]
  test2[,xy:=x*y]
  test2[,cls:=ifelse(x2+y2-0.6 >=0, 1, -1)]
  coef(lm(cls ~ x + y + x2 + y2 + xy, data=test2))
}), recursive=F), ncol=6, nrow=100, byrow=T)

apply(weigths, 2, mean)
