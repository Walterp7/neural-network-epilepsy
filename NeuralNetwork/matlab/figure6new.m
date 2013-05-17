%fig3-3
%set(0,'DefaultAxesFontName', 'Arial');
%set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
%set(0,'DefaultTextFontname', 'Arial');
%set(0,'DefaultTextFontSize', 12);

figure;
%%%BIC
%comp





subplot(5,2,1)

lfp_data2 = load(['lfp_contr_focal.csv']);
min_time = 100;
max_time =300;
[b,a] = butter(8, 2*150/10000,'low')
 for col = 2:size(lfp_data2,2)
     lfp_data2(:, col) = filter(b,a,lfp_data2(:,col));   
 end;
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
 lfp2 = lfp_data2(start_index:end_index,:);   
plot(lfp2(:,1), lfp2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
ylim([-30, 10])
%plot(lfp_data2(:,1), lfp_data2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
set(gca,'Box','off')
%set(gca,'YTick', [-30 -20 10])
%set(gca,'YTickLabel',{'10','0', '-30'})
set(gca,'YDir','reverse')
xlabel('time [ms]')

subplot(5,2,3)
lfp_data2 = load(['lfp_contr_blockade20.csv']);
[b,a] = butter(8, 2*150/10000,'low')
 for col = 2:size(lfp_data2,2)
     lfp_data2(:, col) = filter(b,a,lfp_data2(:,col));   
 end;
min_time = 100;
max_time =300;
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
 lfp2 = lfp_data2(start_index:end_index,:);   
plot(lfp2(:,1), lfp2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
ylim([-30, 10])
set(gca,'Box','off')
%set(gca,'YTick', [-30 -20 10])
%set(gca,'YTickLabel',{'10','0', '-30'})
set(gca,'YDir','reverse')
xlabel('time [ms]')

subplot(5,2,5)
lfp_data2 = load(['lfp_contr_blockade30.csv']);
[b,a] = butter(8, 2*150/10000,'low')
 for col = 2:size(lfp_data2,2)
     lfp_data2(:, col) = filter(b,a,lfp_data2(:,col));   
 end;
min_time = 100;
max_time =400;
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
 lfp2 = lfp_data2(start_index:end_index,:);   
plot(lfp2(:,1), lfp2(:,3), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on');
ylim([-30, 15])
set(gca,'Box','off')
%set(gca,'YTick', [-30 -20 10])
%set(gca,'YTickLabel',{'10','0', '-30'})
set(gca,'YDir','reverse')
xlabel('time [ms]') 

subplot(5,2,7)

%biol\
min_time = 0;
max_time =150;

lfp_data1 = load(['Bic control.csv']);
lfp_data2 = load(['lengthening of field with bic.csv']);
lfp_data3 = load(['Bic interictal.csv']);
lfp_data4 = load(['Ictal MUA select region.csv']);



subplot(5,2,2)
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
plot(lfp_data2(start_index:end_index,1), lfp_data2(start_index:end_index,2), 'LineWidth', 1,  'LineSmoothing','on');
ylim([-0.6 0.2])

subplot(5,2,4)
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
plot(lfp_data2(start_index:end_index,1),lfp_data2(start_index:end_index,3), 'LineWidth', 1,  'LineSmoothing','on');
ylim([-1 0.2])

subplot(5,2,6)
start_index = find (lfp_data3(:,1)>=min_time,1, 'first');
end_index = find (lfp_data3(:,1)<= max_time,1, 'last');
plot(lfp_data3(start_index:end_index,1),lfp_data3(start_index:end_index,2), 'LineWidth', 1,  'LineSmoothing','on');
ylim([-0.6 0.2])

subplot(5,2,8)
start_index = find (lfp_data4(:,1)>=min_time,1, 'first');
end_index = find (lfp_data4(:,1)<= max_time,1, 'last');
%plot(lfp_data4(start_index:end_index,1),lfp_data4(start_index:end_index,2), 'LineWidth', 1,  'LineSmoothing','on');
plot(lfp_data4(:,1),lfp_data4(:,2), 'LineWidth', 1,  'LineSmoothing','on');
ylim([-0.6 0.2])
%%%NMDA
%comp

subplot(5,2,9)
min_time = 100;
max_time = 600;
lfp_data2 = load(['lfp_nmda_ampl9.csv']);
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
plot(lfp_data2(start_index:end_index,1),lfp_data2(start_index:end_index,3),  'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on');
set(gca,'YDir','reverse')
%ylim([-1 0.2])
%biol
min_time = 0;
max_time =500;

lfp_data1 = load(['low mag interictal and dur inc.csv']);
start_index = find (lfp_data1(:,1)>=min_time,1, 'first');
end_index = find (lfp_data1(:,1)<= max_time,1, 'last');


subplot(5,2,10)
plot(lfp_data1(start_index:end_index,1), lfp_data1(start_index:end_index,7), 'LineWidth', 1,  'LineSmoothing','on');
ylim([-0.7 0.3])