%fig3-3
set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);

%spikes
spikes_data = load('activity_contr_ampl3.csv');

min_time = 100;
max_time =200;
start_index = find (spikes_data(:,1)>=min_time,1, 'first');
end_index = find (spikes_data(:,1)< max_time,1, 'last');
spikes = spikes_data(start_index:end_index,:);
col_number = 2;
figure;
for col = 1:col_number 

    spikes_temp = spikes(spikes(:,3)== (col-1),:);
    subplot(4,3, [(6*(col-1) +1) (6*(col-1) +4) ]);
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
       if col == 1
            legend('LTS', 'FS',  'IB','RS')
       end;
    
    hold off;
   
end;

%%%% COMPUTATIONAl
lfp_data1 = load('lfp_contr_ampl25.csv');
lfp_data2 = load('lfp_contr_ampl8.csv');


[b,a] = butter(8, 2*550/10000,'low')
for col = 2:size(lfp_data1,2)
   lfp_data2(:, col) = filter(b,a,lfp_data2(:,col));
   lfp_data1(:, col) = filter(b,a,lfp_data1(:,col));
end;


min_time = 100;
max_time =180;
start_index = find (lfp_data1(:,1)>=min_time,1, 'first');
end_index = find (lfp_data1(:,1)<= max_time,1, 'last');
lfp1 = lfp_data1(start_index:end_index,:);
lfp2 = lfp_data2(start_index:end_index,:);
col_number = 3;
lfp1(:,2:(col_number+1)) = lfp1(:,2:(col_number+1)) +20; 
 
lfp2(:,2:(col_number+1)) = lfp2(:,2:(col_number+1)) +20; 
 
ymin = min(min([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))-5
ymax = max(max([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))


% input1 = 7*epsp( lfp1(:,1) - 100);
% input2 = 15*epsp( lfp2(:,1) - 100);
% 
% subplot(6,2,2); 
% plot(lfp1(:,1), input1,  'k', 'LineWidth', 1,  'LineSmoothing','on')
% hold on;
% plot(lfp2(:,1), input2, 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
% xlabel('time [ms]')   
% ylabel( 'Input')
% set(gca,'Box','off')
% hold off;

for col = 2:col_number 
 
subplot(4,3, 3*(col-2) + 2); 
plot((lfp1(:,1)-90), lfp1(:,col+1), 'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot((lfp2(:,1)-90), lfp2(:,col+1), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
hold off;
ylim([ymin, ymax])
xlim([min_time-90 max_time-90])
   set(gca,'YTick', [0 30])
set(gca,'YTickLabel',{'0','-1'})
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   
end;

leg = legend('stim. ampl. 25', 'stim. ampl.3')
set(leg, 'Location', 'SouthEast')
set(leg, 'Box', 'off')
% figure;
% for col = 1:col_number 
%  
% subplot(2,1,col); 
% plot(lfp1(:,1), lfp1(:,col+1), 'k', 'LineWidth', 1,  'LineSmoothing','on')
% hold on;
% plot(lfp2(:,1), lfp2(:,col+1), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
% hold off;
% ylim([ymin, ymax])
% ylabel('LFP' )
% set(gca,'Box','off')
% set(gca,'YDir','reverse')
% xlabel('time [ms]')   
% end;
%%%% BIOLOGY
lfp_data_adj = load('Fig3B adj col.csv');
lfp_data_stim = load('Fig3B stim col.csv');
min_time =0;
max_time =50;
start_index = find (lfp_data_adj(:,1)>=min_time,1, 'first');
end_index = find (lfp_data_adj(:,1)<= max_time,1, 'last');
lfp1 = [lfp_data_stim(start_index:end_index,1:2) , lfp_data_adj(start_index:end_index,2)]

%lfp1( (lfp1(:,1)> 9.9 & lfp1(:,1)<10.2 ),3 ) =0;
%lfp1( (lfp1(:,1)> 9.9 & lfp1(:,1)<10.1 ),2 ) =0;

lfp2 = [lfp_data_stim(start_index:end_index,1),lfp_data_stim(start_index:end_index,3) , lfp_data_adj(start_index:end_index,3)]
col_number = 2;
%lfp2( (lfp2(:,1)> 9.9 & lfp1(:,1)<10.2 ),3 ) =0;
%lfp2( (lfp2(:,1)> 9.9 & lfp1(:,1)<10.1 ),2 ) =0;

ymin = min(min([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))
ymax = max(max([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))





for col = 1:col_number 
 
subplot(4,3, 9 - 3*col); 
plot(lfp2(:,1), lfp2(:,col+1), 'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp1(:,1), lfp1(:,col+1), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
hold off;
ylim([-1, 0.4])
 set(gca,'YTick', [-1 0])
set(gca,'YTickLabel',{'-1','0'})
ylabel('[mV]')
set(gca,'Box','off')
xlim([min_time max_time])
xlabel('time [ms]')   
end;

%figure;
peak_data = load('Fig3C mV.csv');
for i = 2
     subplot(4,3, [9  12])

    %subplot(1,2,1)
   plot(peak_data(:,1), peak_data(:,i),'-rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',8);
   hold on;
   plot(peak_data(:,1), peak_data(:,i+1),'--bd','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',8);
   hold off;
   ylabel('Peak Neg [mV]');
   xlabel('Stimulus intensity level');
 

   set(gca,'Box','off')
 
end;
legend_handle = legend('stimulated', 'adjacent')
 set(legend_handle, 'Box', 'off')
set(legend_handle, 'Location', 'NorthEast')

peak_data = load('computational_peak_neg.csv');
maxl = size(peak_data,1)

       subplot(4,3, [i+6  i+9])
 % subplot(1,2,2);
   plot(peak_data(1:maxl,1), peak_data(1:maxl,3),'-rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',8);
   hold on;
   plot(peak_data(1:maxl,1), peak_data(1:maxl,2),'--bd','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',8);
 set(gca,'Box','off')
 set(gca,'YTick', [-30 -24 -18 -12 -6 0])
set(gca,'YTickLabel',{'-1','-0.8','-0.6', '-0.4', '-0.2','0'})
   hold off;
   ylabel('Peak Neg [mV]')
   xlabel('Stimulus intensity level')
  
%legend( 'Stimulated (col 2)','Adjacent (col 1)');






