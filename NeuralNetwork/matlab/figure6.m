set(0,'DefaultAxesFontName', 'Arial')
set(0,'DefaultAxesFontSize', 12)

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial')
set(0,'DefaultTextFontSize', 12)

lfp_data1 = load('lfp_blockade_blockade10.csv');


min_time = 10;
max_time =1000;
start_index = find (lfp_data1(:,1)>=min_time,1, 'first');
end_index = find (lfp_data1(:,1)<= max_time,1, 'last');
lfp1 = lfp_data1(start_index:end_index,:);




figure;

for lev = 4:8 
 level = lev*10
 lfp_data2 = load(['lfp_blockade_blockade', int2str(level), '.csv']);
 lfp2 = lfp_data2(start_index:end_index,:);   
ymin = min(min([lfp1(:, 3); lfp2(:, 3)]));
ymax = max(max([lfp1(:, 3); lfp2(:, 3)] ));
%subplot(6,2,(lev-2)*2-1); 
subplot(5,2,(lev-3)*2-1);
plot(lfp1(:,1), lfp1(:,3), 'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp2(:,1), lfp2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
hold off;
ylim([ymin, ymax])
ylabel([int2str(level), '%'] )
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   
end;

spikes_data = load('activity_test_blockade50.csv');
start_index = find (spikes_data(:,1)>=min_time,1, 'first');
end_index = find (spikes_data(:,1)< max_time,1, 'last');
spikes = spikes_data(start_index:end_index,:);
col_number = 5;

for col = 1:col_number 

    spikes_temp = spikes(spikes(:,3)== (col-1),:);
    subplot(5,2, col*2);
    scatter(spikes_temp(spikes_temp(:,4)==1, 1), spikes_temp(spikes_temp(:,4)==1, 2), '.r');
    hold on;
    scatter(spikes_temp(spikes_temp(:,4)==2, 1), spikes_temp(spikes_temp(:,4)==2, 2), '.b');
     hold on;
    scatter(spikes_temp(spikes_temp(:,4)==4, 1), spikes_temp(spikes_temp(:,4)==4, 2),'Marker', '.', 'MarkerEdgeColor',[0,0.6,0.2]);
    hold on;
    scatter(spikes_temp(spikes_temp(:,4)==3, 1), spikes_temp(spikes_temp(:,4)==3, 2), '.g');
  
    %layer markings
    VItop = max(spikes_temp(spikes_temp(:,5) == 6,2));
    Vtop = max(spikes_temp(spikes_temp(:,5) == 5,2));
    IVtop = max(spikes_temp(spikes_temp(:,5) == 4,2));
    IIItop = max(spikes_temp(spikes_temp(:,5) == 3,2));

    
    minCol = min( spikes_data(spikes_data(:,3)== (col-1),2));
    maxCol = max( spikes_data(spikes_data(:,3)== (col-1),2));
     set(gca,'YTick', [(VItop-minCol)/2 + minCol , (Vtop - VItop)/2 + VItop , (IVtop-Vtop)/2+ Vtop, (IIItop-IVtop)/2+ IVtop])
     set(gca,'YTickLabel',{'VI','V','IV','II/III'})
      line( [min_time max_time], [ VItop,  VItop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
      line( [min_time max_time], [ Vtop,  Vtop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
      line( [min_time max_time], [ IVtop,  IVtop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
      line( [min_time max_time], [ IIItop,  IIItop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
    %axes labels
     xlabel('time [ms]');
    ylabel(['Column ', int2str(col)]);
    
   % xlim([min_time max_time]) ;
    ylim( [minCol, maxCol]);
       
    
    hold off;
   
end;
