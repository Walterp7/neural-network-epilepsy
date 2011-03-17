clear all;
E = load('outputAllNeurons.txt');
a = load('outputSpikes.txt');
figure;

startNeuron = 1;
endNeuron = 4*764;

RS = a(a(:,3)==0,:);
IB = a(a(:,3)==1,:);
FS = a(a(:,3)==2,:);
LTS = a(a(:,3)==3,:);

subplot(6,1,1);
start = 100;
value = 20;
sTime = 20;
pickInput =[zeros(1,start),value*ones(1, sTime), zeros(1, 1000 - sTime- start)]; 
 %plot(1:1000,pickInput,'m');
 %ylim([-1 24]);
 T = 1:1000;
 step = zeros(1,1000);
 %step = 5*(heaviside(-155+T).*heaviside(165-T)+heaviside(-320+T).*heaviside(330-T)+heaviside(-485+T).*heaviside(495-T)+heaviside(-650+T).*heaviside(660-T)+heaviside(-815+T).*heaviside(825-T)+heaviside(-980+T).*heaviside(990-T) );
tmp = 1:20;
for i = tmp
   step = step+5* heaviside(-(i-1)*50-40+T).*heaviside(i*50-T);
end

 plot(1:1000,step,'m');
 ylim([-1 10]);
 title('input to RS and FS neurons in 2nd column VI layer');
 

subplot(6,1,2:5);
hold on;
plot(RS(:,1),RS(:,2),'.g'); %RS
plot(IB(:,1),IB(:,2),'.k');
%plot(CH(:,1),CH(:,2),'.k');
plot(FS(:,1),FS(:,2),'.b');
plot(LTS(:,1),LTS(:,2),'.r');


%hold off;

title('Network activity');
legend('RS','IB','FS','LTS')
xlim([1 1000]) 
ylim([startNeuron endNeuron]) 
set(gca,'YTick',(startNeuron+350):764:endNeuron)
set(gca,'YTickLabel',{'1 Column','2 Column','3 Column','4 Column','5 Column'})

for i=1:4 
    plot(1:1000,764*i,'--r','Color','red','LineWidth',2);
end
hold off;
subplot(6,1,6);

E(:,2:3)=E(:,2:3)/3000;
plot(E(:,1), E(:,3),'g');
xlabel('time [ms]');
%ylim