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
