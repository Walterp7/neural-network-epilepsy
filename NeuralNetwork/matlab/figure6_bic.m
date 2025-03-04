%fig3-3
set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);

%spikes
spikes_data = load('activity_short__focal.csv');

subplot(4,2,[1 3]);
peak_data = load('pk short latency.csv');

   plot(peak_data(:,1), peak_data(:,2),'-','LineWidth',1,'Color', 'r','Marker', 's', 'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',9);
   hold on;
   plot(peak_data(:,1), peak_data(:,3),'--','LineWidth',1,'Color', 'k','Marker', 'd','MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',9);
   hold on;
     plot(peak_data(:,1), peak_data(:,4),':','LineWidth',1,'Color', 'b','Marker', 'v','MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',9);
   hold on;
   plot(peak_data(:,1), peak_data(:,5),'-.','LineWidth',1,'Color', 'm','Marker', 'o','MarkerEdgeColor','m','MarkerFaceColor','m','MarkerSize',9);
   hold off;
   
   xlabel('Stimulus intensity level');
   title('Biological data');
   legend_handle = legend('Control','uM Bic 10', 'uM Bic 50', 'uM Bic 100')
   set(legend_handle, 'Location', 'NorthEast')
   set(legend_handle, 'Box', 'off')
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
subplot(4,2,[2 4]);
peak_data = load('pk short latency comp.csv');
last_ind = size(peak_data,1);
   plot(peak_data(:,1), peak_data(:,2),'-','LineWidth',1,'Color', 'r','Marker', 's', 'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',9);
   hold on;
   plot(peak_data(:,1), peak_data(:,4),'--','LineWidth',1,'Color', 'k','Marker', 'd','MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',9);
   hold on;
     plot(peak_data(:,1), peak_data(:,5),':','LineWidth',1,'Color', 'b','Marker', 'v','MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',9);
   hold on;
   plot(peak_data(:,1), peak_data(:,6),'-.','LineWidth',1,'Color', 'm','Marker', 'o','MarkerEdgeColor','m','MarkerFaceColor','m','MarkerSize',9);
   
   hold off;
   
   xlabel('Stimulus intensity level');
   title('Biological data');
   legend_handle = legend('Control','30% blockade', '50% blockade', '80% blockade')
   set(legend_handle, 'Location', 'NorthEast')
   set(legend_handle, 'Box', 'off')
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 lfp = load('Bic interictal.csv');
subplot(4,2,5);
plot(lfp(:,1), lfp(:,2), 'k', 'LineWidth', 1,  'LineSmoothing','on')

ylabel('[mV]')
set(gca,'Box','off')
set(gca,'YTick', [-0.6 0.2])
set(gca,'YTickLabel',{'-0.6','0.2'})
ylim([-0.6 0.2])
xlabel('time [ms]')   
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
lfp = load('Bic control.csv');

subplot(4,2,7);
plot(lfp(:,1), lfp(:,2), 'k', 'LineWidth', 1,  'LineSmoothing','on')
ylim([-0.2 0.2])
ylabel('[mV]')
set(gca,'Box','off')
set(gca,'YTick', [-0.2 0.2])
set(gca,'YTickLabel',{'-0.2','0.2'})
xlabel('time [ms]') 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
subplot(4,2, 6)
lfp_data2 = load(['lfp_blockade_blockade50.csv']);
[b,a] = butter(8, 2*150/10000,'low')
 for col = 2:size(lfp_data2,2)
     lfp_data2(:, col) = filter(b,a,lfp_data2(:,col));   
 end;
min_time = 100;
max_time =300;
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
 lfp2 = lfp_data2(start_index:end_index,:);   
ymin = min(min( lfp2(:, 3)));
ymax = max(max(lfp2(:, 3) ));


plot(lfp2(:,1), lfp2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')

ylim([-30, 10])
%ylabel([int2str(level), '%'] )
set(gca,'Box','off')
set(gca,'YTick', [-30 -20 10])
set(gca,'YTickLabel',{'10','0', '-30'})
set(gca,'YDir','reverse')
xlabel('time [ms]')   
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
subplot(4,2, 8)
lfp_data2 = load(['lfp_blockade_blockade60.csv']);
[b,a] = butter(8, 2*150/10000,'low')
for col = 2:size(lfp_data2,2)
    lfp_data2(:, col) = filter(b,a,lfp_data2(:,col));   
end;
min_time = 100;
max_time =300;
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
 lfp2 = lfp_data2(start_index:end_index,:);   
ymin = min(min( lfp2(:, 3)));
ymax = max(max(lfp2(:, 3) ));


plot(lfp2(:,1), lfp2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')

ylim([ymin-5, ymax+5])
%ylabel([int2str(level), '%'] )
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   

set(gca,'YTick', [-30 -20 10])
set(gca,'YTickLabel',{'10','0', '-30'})
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
figure;
lfp = load('nmda_biology.csv');
plot(lfp(:,1), lfp(:,2))


