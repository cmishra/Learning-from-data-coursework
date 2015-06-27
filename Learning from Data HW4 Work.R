# 5/28/2015
# Learning from Data HW #4 Work 

### Methods
library(data.table)
mhApprox <- function(N, dVC, nFactor) {
  (nFactor*N)^dVC
}

# Obviously the following four functions could also be defined in one line each.
# They are defined in these ways to be easier to debug/logically follow
vcBound <- function(N, delta, dVC) {
  ret <- log(4*mhApprox(N, dVC, 2)/delta)
  (8*ret/N)^(1/2)
}

rademacher <- function(N, delta, dVC) {
  (2*log(2*N*mhApprox(N, dVC, 1))/N)^(1/2) + (2/N*log(1/delta))^(1/2) + 1/N
}

parrondo <- function(N, delta, dVC, epsilon) {
  ret <- 4*epsilon*(1+epsilon)+log(4/delta*mhApprox(N, dVC, N))
  (ret/(2*N))^(1/2)
}

devroye <- function(N, delta, dVC, epsilon) {
  ret <- 4*epsilon*(1+epsilon)+log(4*mhApprox(N, dVC, N)/delta)
  ret/(2*N)
}


### Num 1 Work
n_vals <- c(40, 42, 46, 48)*10000
vcBound(n_vals, 0.05, 10)


### Num 2/3 Work
N_levels <- c(5, 10000)
delta_levels <- c(0.1, 0.05, 0.01)
dVC_levels <- c(1, 5, 10)

dat <- data.table(expand.grid(list(N_levels, delta_levels, dVC_levels)))
setnames(dat, c("Var1", "Var2", "Var3"), c("N", "delta", "dVC"))

dat[,vc:=vcBound(N,delta, dVC)]
dat[,rademacher:=rademacher(N, delta, dVC)]
dat[,c("parrondoV", "parrondoR") :=list(parrondo(N, delta, dVC, vc), 
  parrondo(N, delta, dVC, rademacher))]
dat[,c("devroyeV", "devroyeR") :=list(devroye(N, delta, dVC, vc), 
  devroye(N, delta, dVC, rademacher))]

options(digits=4)
dat

unlist(lapply(dat[N==5], mean))[c(1,4:9)]
unlist(lapply(dat[N==10000], mean))[c(1,4:9)]

### Num 6 Validation


weights <- unlist(lapply(1:1000, function(i) {
  input <- runif(2, -1, 1)
  dat <- data.table(x=input, y=sin(pi*input))
  coef(lm(data=dat, y ~ x-1))
}))
mean(weights)
var(weights)