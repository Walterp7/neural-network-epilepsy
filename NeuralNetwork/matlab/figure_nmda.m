set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);
figure;
time = 0:0.1:50;
dt = time -10;
td = 0.5; %r3 f3
tr = 10;
%dt = dt.*heaviside(dt);
epsp1 = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt)/0.6968;
%epsp1(isnan(x)) = 0;

td = 0.5;
tr = 5;
%dt = dt.*heaviside(dt);
epsp2 = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt)/0.6968;
max_epsp = max(max([epsp1, epsp2]))
%epsp2(isnan(x)) = 0;
%%%
subplot(4,2,1)
plot(time, epsp2,'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(time, epsp1,'b', 'LineWidth', 1,  'LineSmoothing','on')
xlabel('time [ms]')

xlim([0 50]);
ylim([0 max_epsp])
set(gca,'Box','off')
set(gca,'YTick', [0 1])
set(gca,'YTickLabel',{'0','1'})
%%%%%%%%%%%%%%%%%%%%%%%%%
td = 0.1; %r4 r4
tr = 8.4;
%dt = dt.*heaviside(dt);
epsp1 = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt)/0.8267;
%epsp1(isnan(x)) = 0;

td = 0.1;
tr = 7;
%dt = dt.*heaviside(dt);
epsp2 = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt)/0.8267;
%epsp2(isnan(x)) = 0;
%%%
max_epsp = max(max([epsp1, epsp2]))
%%%
subplot(4,2,2)
plot(time, epsp2,'k', 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(time, epsp1,'b', 'LineWidth', 1,  'LineSmoothing','on')
xlabel('time [ms]')

xlim([0 50]);
ylim([0 max_epsp])
set(gca,'YTick', [0 1])
set(gca,'YTickLabel',{'0','1'})


min_time = 10;
max_time =1000;
spikes_data = load('activity_nmda_focal.csv');
start_index = find (spikes_data(:,1)>=min_time,1, 'first');
end_index = find (spikes_data(:,1)< max_time,1, 'last');
spikes = spikes_data(start_index:end_index,:);
col_number = 3;



for col = 1:col_number 

    spikes_temp = spikes(spikes(:,3)== (col-1),:);
    subplot(4,2, [col*2+1 col*2+2]) ;
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
       
    
    hold off;
   
end;