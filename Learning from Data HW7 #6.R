library(data.table)

dt <- data.table(a=runif(10E4), b=runif(10E4))
dt[,mean(pmin(a, b))]
