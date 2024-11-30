library(ggplot2)
library(dplyr)

data <- read.csv("benchmark_rainbow_vs_redblack.csv", sep = ";")

# plot data
plot_data <- data
plot_data$operation <- factor(data$operation, labels = c("add", "remove", "mix", "rebuild"))

# redblack

redblack_plot_data <- plot_data[plot_data$tree == 0,]

redblack_plot <- ggplot(redblack_plot_data, aes(x = n, y = time, color = as.factor(operation), group = operation)) +
  geom_point() +
  geom_line() +
  labs(
    x = "n",
    y = "time (ms)",
    title = paste("red-black tree") ,
    color = "operation"
  ) +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 90, hjust = 1)) + 
  ylim(0, 1750)


print(redblack_plot)

# rainbow

rainbow_plot_data <- plot_data[plot_data$tree == 1,]

rainbow_plot <- ggplot(rainbow_plot_data, aes(x = n, y = time, color = as.factor(operation), group = operation)) +
  geom_point() +
  geom_line() +
  labs(
    x = "n",
    y = "time (ms)",
    title = paste("rainbow tree with k", "=", rainbow_plot_data$k[[1]]) ,
    color = "operation"
  ) +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 90, hjust = 1)) + 
  ylim(0, 1750)


print(rainbow_plot)