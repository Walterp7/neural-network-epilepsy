set(0,'DefaultAxesFontName', 'Arial')
set(0,'DefaultAxesFontSize', 12)

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial')
set(0,'DefaultTextFontSize', 12)

%partA

%lfp_A = []; %1hz
spikes_data = load('activity_f_frequency_inputer.csv');
min_time = 800;
max_time =5000;
start_index = find (spikes_data(:,1)>=min_time,1, 'first');
end_index = find (spikes_data(:,1)< max_time,1, 'last');
spikes = spikes_data(start_index:end_index,:);
%lfp_A = lfp_A(start_index:end_index,:);
col_number = 3;
    figure;
for col = 2:col_number 
   subplot(4,1,col - 1);
   
    spikes_temp = spikes(spikes(:,3)== (col-1),:);
 
    scatter(spikes_temp(spikes_temp(:,4)==1, 1), spikes_temp(spikes_temp(:,4)==1, 2), '.r');
    hold on;
    scatter(spikes_temp(spikes_temp(:,4)==2, 1), spikes_temp(spikes_temp(:,4)==2, 2), '.b');
     hold on;
    scatter(spikes_temp(spikes_temp(:,4)==4, 1), spikes_temp(spikes_temp(:,4)==4, 2),'Marker', '.', 'MarkerEdgeColor',[0,0.6,0.2]);
    hold on;
    scatter(spikes_temp(spikes_temp(:,4)==3, 1), spikes_temp(spikes_temp(:,4)==3, 2), '.g');
  
    %layer markings
    VItop = max(spikes_temp(spikes_temp(:,5) == 6,2))
    Vtop = max(spikes_temp(spikes_temp(:,5) == 5,2));
    IVtop = max(spikes_temp(spikes_temp(:,5) == 4,2));
    IIItop = max(spikes_temp(spikes_temp(:,5) == 3,2));

    %axes
    minCol = min( spikes_temp(:,2));
    maxCol = max( spikes_temp(:,2));
    set(gca,'YTick', [(VItop-minCol)/2 + minCol , (Vtop - VItop)/2 + VItop , (IVtop-Vtop)/2+ Vtop, (IIItop-IVtop)/2+ IVtop]);
    set(gca,'YTickLabel',{'VI','V','IV','II/III'});
   
    line( [min_time max_time], [ VItop  VItop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
     line( [min_time max_time], [ Vtop,  Vtop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
    line( [min_time max_time], [ IVtop,  IVtop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
    line( [min_time max_time], [ IIItop,  IIItop], 'LineStyle',':', 'Color', [0.5,0.5,0.5])
    %axes labels
     xlabel('time [ms]');
      xlim([min_time max_time]) 
    %ylabel(['Column ', int2str(col)]);
    
    xlim([min_time max_time]) 
    ylim( [minCol, maxCol])
     
    
    hold off;
   
end;



%subplot(2,1,2);

% plot(lfp_A(:,1), lfp_A(:,col_number+1), 'b')
% ylabel('LFP' )
% set(gca,'Box','off')
% set(gca,'YDir','reverse')
% xlabel('time [ms]')   

%partB
%figure;
lfp_data1 = load('lfp_short__focal_8.csv');
lfp_data2 = load('lfp_LTSblockade_LTSblockade50.csv');
lfp_data3 = load('lfp_LTSblockade_LTSblockade80.csv');
min_time = 10;
max_time =500;
start_index = find (lfp_data1(:,1)>=min_time,1, 'first');
end_index = find (lfp_data1(:,1)<= max_time,1, 'last');
lfp1 = lfp_data1(start_index:end_index,:);
lfp2 = lfp_data2(start_index:end_index,:);
lfp3 = lfp_data3(start_index:end_index,:);
col_number = 3;

ymin = min(min([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1)); lfp3(:, 2:(col_number+1))]))
ymax = max(max([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1)); lfp3(:, 2:(col_number+1))]))




for col = 2:col_number 
 
subplot(4,1,col+1); 
plot(lfp1(:,1), lfp1(:,col+1), 'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp2(:,1), lfp2(:,col+1), 'Color', [1,0,0], 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp3(:,1), lfp3(:,col+1), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
hold off;
ylim([ymin, ymax]);
ylabel(['Column ', int2str(col)] );
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   
set(gca,'YTick', [-20 -10])
set(gca,'YTickLabel',{'0','-1'})
end;
 legend_handle = legend('control', '50% LTS blockade',  '80% LTS blockade', 'Box', 'off')
 set(legend_handle, 'Box', 'off')
set(legend_handle, 'Location', 'SouthWest')
