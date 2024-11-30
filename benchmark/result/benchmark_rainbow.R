library(ggplot2)
library(dplyr)

data <- read.csv("benchmark_rainbow.csv", sep = ";")

# plot data
plot_data <- data
plot_data$k <- as.factor(data$k)
plot_data$operation <- factor(data$operation, labels = c("add", "remove", "mix", "rebuild"))

my_plot <- ggplot(plot_data, aes(x = k, y = time, color = as.factor(operation), group = operation)) +
  geom_point() +
  geom_line() +
  labs(
    x = "k",
    y = "time (ms)",
    title = paste("n", "=" , data$n[1]) ,
    color = "operation"
  ) +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 90, hjust = 1))


print(my_plot)

# get most efficient k
sum_times_table <- data %>%
  group_by(k) %>%
  summarise(sum_times = sum(time))

best_k_table <- sum_times_table %>% filter(sum_times == min(sum_times))
best_k <- best_k_table$k
best_sum_times <- best_k_table$sum_times

print(paste(best_k, "with sum of times:", best_sum_times, "ms"))