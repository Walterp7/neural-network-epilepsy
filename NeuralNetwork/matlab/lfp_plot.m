min_time = 10;
max_time =500;
start_index = find (lfp_data(:,1)>=min_time,1, 'first');
end_index = find (lfp_data(:,1)<= max_time,1, 'last');
lfp1 = lfp_data1(start_index:end_index,:);
lfp2 = lfp_data2(start_index:end_index,:);
col_number = 3;

ymin = min(min([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))
ymax = max(max([lfp1(:, 2:(col_number+1)); lfp2(:, 2:(col_number+1))]))

%input1 = zeros(size(lfp1(:,1),1),1);
%input2 = zeros(size(lfp1(:,1),1),1);
input1 = 7*epsp( lfp1(:,1) - 100);
input2 = 15*epsp( lfp2(:,1) - 100);

subplot(col_number,1,1); 
plot(lfp1(:,1), input1, 'b')
hold on;
plot(lfp2(:,1), input2, 'k')
xlabel('time [ms]')   
ylabel( 'Input')
set(gca,'Box','off')
hold off;

for col = 2:col_number 
 
subplot(col_number,1,col); 
plot(lfp1(:,1), lfp1(:,col+1), 'b')
hold on;
plot(lfp2(:,1), lfp2(:,col+1), 'k')
hold off;
ylim([ymin, ymax])
ylabel('LFP' )
set(gca,'Box','off')
set(gca,'YDir','reverse')
xlabel('time [ms]')   
end;