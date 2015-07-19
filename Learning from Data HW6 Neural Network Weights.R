
# Problem 9/10

### following function relates number of nodes in first hidden layer to  overall num of weights given
### constraints of the neural network described in problems 9/10 of
### https://work.caltech.edu/homework/hw6.pdf
### Assumes that if you have less than 17 nodes in layer 1, nodes are laid out symmetrically
numWeights <- function(x) {
  if (x <= 16)
    10*(x-1)+(x-1)^2*(36/x-1)+(36/x-1)*(x-1)+x
  else
    10*(x-1)+(x-1)*(36-x-1)+(36-x-1)+36-x
}

### Let's plot to find min/max
plot(1:36, unlist(lapply(1:36, numWeights)))

### Or if you don't like pictures
which.min(unlist(lapply(1:36, numWeights))) # min is at 1 node per layer
which.max(unlist(lapply(1:36, numWeights))) # max is at 23 nodes



