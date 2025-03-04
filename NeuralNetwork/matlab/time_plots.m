set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);
set(gca,'Box','off')
% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);
figure;
%%%% TIME vs COLUMNS
subplot(3,4, [1 2]);
data_cols = load('time_vs_cols.csv');
%scatter
%plot(data_cols(:,1), data_cols(:,4), '--rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',10);
errorbar(data_cols(:,1), data_cols(:,4), data_cols(:,5), '--rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',10);
ylabel('time [s]');
xlabel('number of columns')
xlim( [1 10])

%%%% TIME vs NOISE
subplot(3,4, [5 6]);
data_noise = load('time_vs_noise.csv');
errorbar(data_noise(:,1), data_noise(:,4), data_noise(:,5), '--rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',10);
ylabel('time [s]');
xlabel('level of noise')
xlim( [5 30])

%%%% TIME vs TIME
subplot(3,4,[7 8]);
data_time = load('time_vs_time.csv');
errorbar(data_time(:,1)/1000, data_time(:,4), data_time(:,5), '--rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',10);
ylabel('time [s]');
xlabel('total time of simulation [s]')
xlim( [0 10])

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%BUILDING TIME


%%%% TIME vs COLUMNS
subplot(3,4, [3 4]);
errorbar(data_cols(:,1), data_cols(:,2), data_cols(:,3), '--ks','LineWidth',2,'MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',10);
ylabel('time [s]');
xlabel('number of columns')
xlim( [1 10])


% %%%% TIME vs NOISE
% subplot(3,4,7);
% 
% errorbar(data_noise(:,1), data_noise(:,2), data_noise(:,3), '--ks','LineWidth',2,'MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',10);
% ylabel('time [s]');
% xlabel('level of noise')
% ylim( [0.5 1.5])
% xlim( [5 30])
% 
% %%%% TIME vs TIME
% subplot(3,4,8);
% 
% errorbar(data_time(:,1)/1000, data_time(:,2), data_time(:,3), '--ks','LineWidth',2,'MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',10);
% ylabel('time [s]');
% xlabel('total time of simulation [s]')
% ylim( [0.5 1.5])
% xlim( [0 10])
% 
% subplot(3,4,[11 12]);
% plot(data_noise(:,1), data_noise(:,6)/10000,'--bs','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',10);
% xlim( [5 30])
% ylabel('number of spikes [10^4]');
% xlabel('level of noise')
