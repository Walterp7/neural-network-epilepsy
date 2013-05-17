set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);
intensities = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25];
dt = 0.1;

%RS IB FS LTS
a = [0.02;0.02;0.1;0.02];
b = [0.2;0.2;0.2;0.25];
c = [-65;-55;-65;-65];
d = [8;4;2;2];

results = zeros(size(intensities,2),4);
results2 = zeros(size(intensities,2),4);
maxRate = [1000/160;1000/300;1000/350;1000/212];
for i = 1: size(intensities,2)
    
v2=-65*ones(4,1); % Initial values of v
u2=b.*v2; % Initial values of u
%firings2=[]; % spike timings
timings = -10*ones(4,1);

v=-65*ones(4,1); % Initial values of v
u=b.*v; % Initial values of u
%firings=[]; % spike timings
I=intensities(1,i)*ones(4,1); % thalamic input
for t=1:1000 % simulation of 100 ms
  
    fired=find(v>=30); % indices of spikes
    %firings=[firings; t+0*fired,fired];
    results(i,fired) = results(i,fired)+1;
    v(fired)=c(fired);
    u(fired)=u(fired)+d(fired);
    I1=I;
    v=v+dt*(0.04*v.^2+5*v+140-u+I1); 
  
    u=u+a.*(b.*v-u); % stability


    currentTime = t*dt*ones(4,1);
    fired2=find((v2>=30) & (( currentTime - timings )> maxRate)); % indices of spikes
    results2(i,fired2) = results2(i,fired2)+1;
    timings(fired2) =  currentTime(fired2);
    prefired = find(v2>=30);
    v2(prefired) = reset(prefired);
    %firings2=[firings2; t+0*fired2,fired2];
    v2(fired2)=c(fired2);
    u2(fired2)=u2(fired2)+d(fired2);
    I2=I;
    v2=v2+dt*(0.04*v2.^2+5*v2+140-u2+I2); % 
  
    u2=u2+a.*(b.*v2-u2); % stability
end;
end;
figure;
subplot(2,1,1)
plot(intensities, results(:,1)*10, '-gs','LineWidth',2,'MarkerEdgeColor','g','MarkerFaceColor','g','MarkerSize',8);
hold on;
plot(intensities, results(:,2)*10,'--k>','LineWidth',2,'MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',8);
hold on;
plot(intensities, results(:,3)*10,':bd','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',8);
hold on;
plot(intensities, results(:,4)*10, '-.ro','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',8);
xlabel('intensity [mV]');
ylabel('frequency [Hz]');
l_handle = legend('RS', 'IB', 'FS', 'LTS');
 set(l_handle, 'Box', 'off');
set(l_handle, 'Location', 'NorthWest');
set(gca,'Box','off')
hold off;
subplot(2,1,2)
plot(intensities, results2(:,1)*10,'-gs','LineWidth',2,'MarkerEdgeColor','g','MarkerFaceColor','g','MarkerSize',8);
hold on;
plot(intensities, results2(:,2)*10,'--k>','LineWidth',2,'MarkerEdgeColor','k','MarkerFaceColor','k','MarkerSize',8);
hold on;
plot(intensities, results2(:,3)*10,':bd','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',8);
hold on;
plot(intensities, results2(:,4)*10,'-.ro','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',8);
xlabel('intensity [mV]');
ylabel('frequency [Hz]');
set(gca,'Box','off')
hold off;
