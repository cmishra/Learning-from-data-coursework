setwd("../Dropbox/College/3_Third Year/Summer Learning/Learning From Data/")
library(data.table)
library(ggplot2)

tests <- lapply(1:6, function(i) data.table(read.csv(paste0("hw5-6-7Programs/testFile", i, ".csv"), header=F)))

invisible(lapply(tests, function(dt) dt[,V7:=V3 == V6]))

invisible(lapply(1:6, function(i) tests[[i]][,"testRunNum":=i]))

test <- rbindlist(tests)

ggplot(test, aes(V1, V2, shape=factor(V3), col=V4, size=factor(-1*V7))) +
  geom_point() +
  facet_wrap(~testRunNum) +
  scale_size_discrete(range=c(2,3))
