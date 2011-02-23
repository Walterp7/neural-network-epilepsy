function  plotOneColumn( a, columnNumber )

startNeuron = 1+(columnNumber-1)*764;
endNeuron = 764*columnNumber;
neuronIndx = find(startNeuron <a(:,2)< endNeuron);

neuronsInLayer = [212,110, 165,277];

RS = a(a(neuronIndx,3)==1,:);
IB = a(a(neuronIndx,3)==2,:);
%CH = a(a(neuronIndx,3)==3,:);
FS = a(a(neuronIndx,3)==4,:);
LTS = a(a(neuronIndx,3)==5,:);



figure;
hold on;
plot(RS(:,1),RS(:,2),'.g'); 
plot(IB(:,1),IB(:,2),'.m');
%plot(CH(:,1),CH(:,2),'.k');
plot(FS(:,1),FS(:,2),'.b');
plot(LTS(:,1),LTS(:,2),'.r');

xlabel('time [ms]');
title('Network activity (one column) ');
legend('RS','IB','FS','LTS', 'Location','EastOutside')
xlim([1 1000]) 
ylim([startNeuron endNeuron]) 
set(gca,'YTick',(startNeuron+100):150:(endNeuron-150))
set(gca,'YTickLabel',{'II/III layer ','IV layer','V layer','VI layer'})

neurons =0;
for i=1:4
    neurons = neurons + neuronsInLayer(i);
    plot(1:1000,neurons+startNeuron,'--r','Color','red','LineWidth',2);
end
hold off;
end
