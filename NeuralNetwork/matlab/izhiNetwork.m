% Created by Eugene M. Izhikevich, February 25, 2003
% Excitatory neurons Inhibitory neurons
figure;



Ne=800; Ni=200;
re=rand(Ne,1); ri=rand(Ni,1);
a=[0.02*ones(Ne,1); 0.02+0.08*ri];
b=[0.2*ones(Ne,1); 0.25-0.05*ri];
c=[-65+15*re.^2; -65*ones(Ni,1)];
d=[8-6*re.^2; 2*ones(Ni,1)];
S=[0.5*rand(Ne+Ni,Ne), -rand(Ne+Ni,Ni)];


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% regular
v2=-65*ones(Ne+Ni,1); % Initial values of v
u2=b.*v2; % Initial values of u
firings2=[]; % spike timings
timings = -10*ones(Ne+Ni,1);
%maxRate = [1000/160 * (ones(Ne,1) ;1000/350*ones(Ni,1)];
maxRate = [1000/160 * (0.2*rand(Ne,1)+0.9) ;1000/350*(0.2*rand(Ni,1)+0.9)];


v=-65*ones(Ne+Ni,1); % Initial values of v
u=b.*v; % Initial values of u
firings=[]; % spike timings
for t=1:1000 % simulation of 1000 ms
I=[5*randn(Ne,1);2*randn(Ni,1)]; % thalamic input
fired=find(v>=30); % indices of spikes
firings=[firings; t+0*fired,fired];
v(fired)=c(fired);
u(fired)=u(fired)+d(fired);
I1=I+sum(S(:,fired),2);
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % step 0.5 ms
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % for numerical
u=u+a.*(b.*v-u); % stability


currentTime = t*ones(Ne+Ni,1);
fired2=find((v2>=30) & (( currentTime - timings )> maxRate)); % indices of spikes
timings(fired2) =  currentTime(fired2);
prefired = find(v2>=30);
v2(prefired) = reset(prefired);
firings2=[firings2; t+0*fired2,fired2];
v2(fired2)=c(fired2);
u2(fired2)=u2(fired2)+d(fired2);
I2=I+sum(S(:,fired2),2);
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % step 0.5 ms
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % for numerical
u2=u2+a.*(b.*v2-u2); % stability
end;
subplot(4,2,1)
plot(firings(:,1),firings(:,2),'.k', 'MarkerSize', 5);
title('Original network - Izhikevich neuron model')
subplot(4,2,2)
plot(firings2(:,1),firings2(:,2),'.k', 'MarkerSize', 5);
title('Original network - modified neuron model')
%%%%%%% increased input
input1 = zeros(1,1000);
input2 = zeros(1,1000);

v2=-65*ones(Ne+Ni,1); % Initial values of v
u2=b.*v2; % Initial values of u
firings2=[]; % spike timings
timings = -10*ones(Ne+Ni,1);


v=-65*ones(Ne+Ni,1); % Initial values of v
u=b.*v; % Initial values of u
firings=[]; % spike timings
for t=1:1000 % simulation of 1000 ms
I=[2+5*randn(Ne,1);2+2*randn(Ni,1)]; % thalamic input
fired=find(v>=30); % indices of spikes
firings=[firings; t+0*fired,fired];
v(fired)=c(fired);
u(fired)=u(fired)+d(fired);
I1=I+sum(S(:,fired),2);
input1(t)=sum(I1(1:Ne,1));
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % step 0.5 ms
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % for numerical
u=u+a.*(b.*v-u); % stability


currentTime = t*ones(Ne+Ni,1);
fired2=find((v2>=30) & (( currentTime - timings )> maxRate)); % indices of spikes
timings(fired2) =  currentTime(fired2);
prefired = find(v2>=30);
v2(prefired) = reset(prefired);
firings2=[firings2; t+0*fired2,fired2];
v2(fired2)=c(fired2);
u2(fired2)=u2(fired2)+d(fired2);
I2=I+sum(S(:,fired2),2);
input2(t)=sum(I2(1:Ne,1));
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % step 0.5 ms
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % for numerical
u2=u2+a.*(b.*v2-u2); % stability
end;
subplot(4,2,3)
plot(firings(:,1),firings(:,2),'.k', 'MarkerSize', 5);;
title('Increased input - Izhikevich neuron model')
subplot(4,2,4)
plot(firings2(:,1),firings2(:,2),'.k', 'MarkerSize', 5);
title('Increased input - modified neuron model')

%%%%%Inhibitory blockade 20%

S=[0.5*rand(Ne+Ni,Ne), -0.8*rand(Ne+Ni,Ni)];
v2=-65*ones(Ne+Ni,1); % Initial values of v
u2=b.*v2; % Initial values of u
firings2=[]; % spike timings
timings = -10*ones(Ne+Ni,1);


v=-65*ones(Ne+Ni,1); % Initial values of v
u=b.*v; % Initial values of u
firings=[]; % spike timings
for t=1:1000 % simulation of 1000 ms
I=[5*randn(Ne,1);2*randn(Ni,1)]; % thalamic input
fired=find(v>=30); % indices of spikes
firings=[firings; t+0*fired,fired];
v(fired)=c(fired);
u(fired)=u(fired)+d(fired);
I1=I+sum(S(:,fired),2);
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % step 0.5 ms
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % for numerical
u=u+a.*(b.*v-u); % stability


currentTime = t*ones(Ne+Ni,1);
fired2=find((v2>=30) & (( currentTime - timings )> maxRate)); % indices of spikes
timings(fired2) =  currentTime(fired2);
prefired = find(v2>=30);
v2(prefired) = reset(prefired);
firings2=[firings2; t+0*fired2,fired2];
v2(fired2)=c(fired2);
u2(fired2)=u2(fired2)+d(fired2);
I2=I+sum(S(:,fired2),2);
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % step 0.5 ms
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % for numerical
u2=u2+a.*(b.*v2-u2); % stability
end;

subplot(4,2,5)
plot(firings(:,1),firings(:,2),'.k', 'MarkerSize', 5);
title('Inhibitory blockade 20% - Izhikevich neuron model')
subplot(4,2,6)
plot(firings2(:,1),firings2(:,2),'.k', 'MarkerSize', 5);
title('Inhibitory blockade 20% - modified neuron model')


%%%%%Inhibitory blockade 50%

S=[0.5*rand(Ne+Ni,Ne), -0.5*rand(Ne+Ni,Ni)];
v2=-65*ones(Ne+Ni,1); % Initial values of v
u2=b.*v2; % Initial values of u
firings2=[]; % spike timings
timings = -10*ones(Ne+Ni,1);


v=-65*ones(Ne+Ni,1); % Initial values of v
u=b.*v; % Initial values of u
firings=[]; % spike timings
for t=1:1000 % simulation of 1000 ms
I=[5*randn(Ne,1);2*randn(Ni,1)]; % thalamic input
fired=find(v>=30); % indices of spikes
firings=[firings; t+0*fired,fired];
v(fired)=c(fired);
u(fired)=u(fired)+d(fired);
I1=I+sum(S(:,fired),2);
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % step 0.5 ms
v=v+0.5*(0.04*v.^2+5*v+140-u+I1); % for numerical
u=u+a.*(b.*v-u); % stability


currentTime = t*ones(Ne+Ni,1);
fired2=find((v2>=30) & (( currentTime - timings )> maxRate)); % indices of spikes
timings(fired2) =  currentTime(fired2);
prefired = find(v2>=30);
v2(prefired) = reset(prefired);
firings2=[firings2; t+0*fired2,fired2];
v2(fired2)=c(fired2);
u2(fired2)=u2(fired2)+d(fired2);
I2=I+sum(S(:,fired2),2);
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % step 0.5 ms
v2=v2+0.5*(0.04*v2.^2+5*v2+140-u2+I2); % for numerical
u2=u2+a.*(b.*v2-u2); % stability
end;

subplot(4,2,7)
plot(firings(:,1),firings(:,2),'.k', 'MarkerSize', 5);
title('Inhibitory blockade 50% - Izhikevich neuron model')
subplot(4,2,8)
plot(firings2(:,1),firings2(:,2),'.k', 'MarkerSize', 5);
title('Inhibitory blockade 50% - modified neuron model')

figure;
subplot(2,2,1)
plot(input1(100:1000), 'r');
xlim([100 1000])
Fs = length(input1(200:1000));
T = 1/Fs;
L=length(input1);
%NFFT = 2^nextpow2(L);
NFFT = L;
Y = fft(input1,NFFT)/L;
f1 = Fs/2*linspace(0,1,NFFT/2+1);

 result1 = 2*abs(Y(1:NFFT/2+1));


subplot(2,2,3)
plot(f1(10:200),result1(10:200), 'r')
%xlim([10 100])
subplot(2,2,2)
plot(input2(100:1000),'b');
xlim([100 1000])
Fs = length(input2(200:1000));
T = 1/Fs;
L=length(input2);
%NFFT = 2^nextpow2(L);
NFFT = L;
Y = fft(input2,NFFT)/L;
f2 = Fs/2*linspace(0,1,NFFT/2+1);

 result2 = 2*abs(Y(1:NFFT/2+1));


subplot(2,2,4)
plot(f2(10:200),result2(10:200), 'b')
%xlim([10 100])
