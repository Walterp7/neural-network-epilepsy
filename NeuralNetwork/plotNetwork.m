function plotNetwork( a )
startNeuron = 1;
endNeuron = 4*764;

RS = a(a(:,3)==0,:);
IB = a(a(:,3)==1,:);
FS = a(a(:,3)==2,:);
LTS = a(a(:,3)==3,:);



figure;
hold on;
plot(RS(:,1),RS(:,2),'.g'); %RS
plot(IB(:,1),IB(:,2),'.m');
%plot(CH(:,1),CH(:,2),'.k');
plot(FS(:,1),FS(:,2),'.b');
plot(LTS(:,1),LTS(:,2),'.r');


%hold off;
xlabel('time [ms]');
title('Network activity');
legend('RS','IB','FS','LTS', 'X','X')
xlim([1 1000]) 
ylim([startNeuron endNeuron]) 
set(gca,'YTick',(startNeuron+350):764:endNeuron)
set(gca,'YTickLabel',{'1 Column','2 Column','3 Column','4 Column','5 Column'})

for i=1:4 
    plot(1:1000,764*i,'--r','Color','red','LineWidth',2);
end
hold off;
end