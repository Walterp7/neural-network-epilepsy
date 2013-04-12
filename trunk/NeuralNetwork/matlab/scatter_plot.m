%data: time, neuron, column, type, layer
spikes_data = load('activity_short__focal.csv')
min_time = 100;
max_time =150;
start_index = find (spikes_data(:,1)>=min_time,1, 'first')
end_index = find (spikes_data(:,1)< max_time,1, 'last')
spikes = spikes_data(start_index:end_index,:);
col_number = 3;
for col = 1:col_number 

    spikes_temp = spikes(spikes(:,3)== (col-1),:);
    subplot(col_number,1,col);
    scatter(spikes_temp(spikes_temp(:,4)==1, 1), spikes_temp(spikes_temp(:,4)==1, 2), '.r');
    hold on;
    scatter(spikes_temp(spikes_temp(:,4)==2, 1), spikes_temp(spikes_temp(:,4)==2, 2), '.b');
     hold on;
    scatter(spikes_temp(spikes_temp(:,4)==4, 1), spikes_temp(spikes_temp(:,4)==4, 2),'Marker', '.', 'MarkerEdgeColor',[0,0.6,0.2]);
    hold on;
    scatter(spikes_temp(spikes_temp(:,4)==3, 1), spikes_temp(spikes_temp(:,4)==3, 2), '.g');
  
    %layer markings
    VItop = max(spikes_temp(spikes_temp(:,5) == 6,2))
    Vtop = max(spikes_temp(spikes_temp(:,5) == 5,2))
    IVtop = max(spikes_temp(spikes_temp(:,5) == 4,2))
    IIItop = max(spikes_temp(spikes_temp(:,5) == 3,2))
    spikes_temp(spikes_temp(:,2) == 2276,5)
    
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
    
    xlim([min_time max_time]) 
    ylim( [minCol, maxCol])
      if col == 1
           legend('LTS', 'FS',  'IB','RS')
      end;
    
    hold off;
   
end;

