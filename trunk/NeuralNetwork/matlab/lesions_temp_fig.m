%fig3-3
set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);

%spikes


min_time = 100;
max_time =200;
%start_index = find (spikes_data(:,1)>=min_time,1, 'first');
%end_index = find (spikes_data(:,1)< max_time,1, 'last');
%spikes = spikes_data(start_index:end_index,:);

figure;


subplot(5,2, [1,3,5,7,9])
peak_data = load('onlyVcolumns.csv');


 
   plot(peak_data(:,1), peak_data(:,2),'--bs','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',8);
   hold on;
   plot(peak_data(:,1), peak_data(:,3),'-rd','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',8);
   hold on;
   plot(peak_data(:,1), peak_data(:,4),':g^','LineWidth',2,'MarkerEdgeColor','g','MarkerFaceColor','g','MarkerSize',8);
   hold on;
   plot(peak_data(:,1), peak_data(:,5),'--m>','LineWidth',2,'MarkerEdgeColor','m','MarkerFaceColor','m','MarkerSize',8);
   hold on;
   plot(peak_data(:,1), peak_data(:,6),'--k*','LineWidth',2,'MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',8);
   hold off;
   ylabel('Peak Neg [mV]');
   xlabel('Inhibitory blockade');
 

   set(gca,'Box','off')
legend_handle = legend('Col1', 'Col2 (stimulated)', 'Col3', 'Col4', 'Col5');
 set(legend_handle, 'Box', 'off')
set(legend_handle, 'Location', 'SouthWest')


lfp_data = load('lfp_les70V_blockade70-onlyV.csv');
min_time = 150;
max_time =400;
start_index = find (lfp_data(:,1)>=min_time,1, 'first');
end_index = find (lfp_data(:,1)<= max_time,1, 'last');
lfp = lfp_data(start_index:end_index,:);
ymin = min(min([lfp(:, 2:6)]))
ymax = max(max([lfp(:, 2:6)]))+0.5
for col = 1:5
    subplot(5,2, 2*col);
    plot((lfp(:,1)), lfp(:,col+1), 'b', 'LineWidth', 1,  'LineSmoothing','on')
    ylim([ ymin ymax]);
    set(gca,'YDir','reverse')
    ylabel(['col ', int2str(col)])
end;
