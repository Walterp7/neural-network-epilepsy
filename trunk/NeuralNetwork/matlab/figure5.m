set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);


%FS blockade
spikes_data = load('activity_final_FSblockade50.csv');
min_time = 10;
max_time =500;
start_index = find (spikes_data(:,1)>=min_time,1, 'first');
end_index = find (spikes_data(:,1)< max_time,1, 'last');
spikes = spikes_data(start_index:end_index,:);
col_number = 3;
figure;
for col = 2:col_number 

    spikes_temp = spikes(spikes(:,3)== (col-1),:);
    subplot(4,2, 2*col -3);
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
    
    xlim([min_time max_time]) ;
    ylim( [minCol, maxCol]);
%       if col == 1
%            legend('LTS', 'FS',  'IB','RS')
%       end;
    
    hold off;
   
end;

lfp_data1 = load('lfp_CONTROL_focal.csv');
lfp_data2 = load('lfp_final_FSblockade50.csv');

start_index = find (lfp_data1(:,1)>=min_time,1, 'first');
end_index = find (lfp_data1(:,1)<= max_time,1, 'last');
lfp1 = lfp_data1(start_index:end_index,:);
lfp2 = lfp_data2(start_index:end_index,:);
col_number = 3;

ymin = min(min([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]));
ymax = max(max([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]));



for col = 2:col_number 
 
subplot(4,2,2*col -2); 
plot(lfp1(:,1), lfp1(:,col+1), 'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp2(:,1), lfp2(:,col+1), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
hold off;
ylim([ymin, ymax]);
xlim([min_time max_time]);
set(gca,'YTick', [-20 10])
set(gca,'YTickLabel',{'0','-1'})
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   
end;
 legend_handle = legend('Control', 'FS blocked')
 set(legend_handle, 'Box', 'off')
set(legend_handle, 'Location', 'SouthEast')


%EEG 
eeg_data1 = load('eeg_control_gamma1.csv');
min_time = 500;
max_time = 1500;
start_index = find (eeg_data1(:,1)>=min_time,1, 'first');
end_index = find (eeg_data1(:,1)<= max_time,1, 'last');
eeg = eeg_data1(start_index:end_index,:);

subplot(4,2,5); 
plot(eeg(:,1)-500, eeg(:,2)/1000+2.5, 'k', 'LineWidth', 0.5,  'LineSmoothing','on')
set(gca,'YTick', [-6 0 6])
set(gca,'YTickLabel',{'-6','0','6'})
set(gca,'Box','off')
xlabel('[ms]')  
ylim([-6 6])

[freq,fft_result] = fft_plot('eeg_control_gamma1.csv', 'b');

subplot(4,2,7); 
plot(freq(10:200), fft_result(10:200), 'k', 'LineWidth', 1,  'LineSmoothing','on')

set(gca,'Box','off')
xlabel('[Hz]') 
ylabel(['Amplitude'])

lfp_data1 = load('lfp_contr_ampl25.csv');
lfp_data2 = load('lfp_final_FSstronger50.csv');
% lfp_data1 = (-1)*lfp_data1 - 20;
% lfp_data2 = (-1)*lfp_data2 - 20;
% size(lfp_data1)
min_time = 10;
max_time = 500;
start_index = find (lfp_data1(:,1)>=min_time,1, 'first');
end_index = find (lfp_data1(:,1)<= max_time,1, 'last');
lfp1 = lfp_data1(start_index:end_index,:);
lfp2 = lfp_data2(start_index:end_index,:);
col_number = 3;

ymin = min(min([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))
ymax = max(max([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))



for col = 2:col_number 
 
subplot(4,2,2*(col +1)); 
plot(lfp1(:,1), lfp1(:,col+1), 'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp2(:,1), lfp2(:,col+1), 'Color', [0.2,0.5,0.8], 'LineWidth', 1,  'LineSmoothing','on')
hold off;
ylim([ymin, ymax]);
xlim([min_time max_time]);
set(gca,'YTick', [-20 10])
set(gca,'YTickLabel',{'0','-1'})
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   
end;
set(gca,'Box','off')
 legend_handle = legend('Control', 'FS increased')
 set(legend_handle, 'Box', 'off')
set(legend_handle, 'Location', 'SouthEast')
