
endTime=3000;
timestep = 0.1;
totalSteps = endTime / timestep;
Y = zeros(4,totalSteps);
U = zeros(4,totalSteps);
X  = zeros(4,totalSteps);
Z = zeros(4,totalSteps);

% ie, ee, ei, ii
t1 = [1.0/3.0 ;1.0/3.0;1.0/3.0;1.0/3.0 ]; % 1/t1
trec = [1.0/100.0; 1.0/800.0; 1.0/800.0; 1.0/100.0] % 1/t
tfac = [0.001;0.001;0.001;1.0/100]% 1/t
UU = [0.04;0.5;0.5;0.04];
notDepressing = [1;0;0;1];
delta = zeros(1,totalSteps);
%delta(100*[1,2,3,4,5])=1;
%delta = ones(1,totalSteps);
delta(1:500:10000)=1;
delta(totalSteps*7/8:500:totalSteps) = 1;
U(:,1)=[0;0;0;0];
X(:,1)=[1;1;1;1];
Y(:,1)=[0;0;0;0];
Z(:,1) = [0;0;0;0];
lastSpike = 0;
for i=2:totalSteps
    if (delta(i)~=0)
        
    end;
    U(:,i) = U(:,i-1)+ 0.1*(-notDepressing.*U(:,i-1).*tfac)+delta(i)*(1-U(:,i-1)).*UU;
    
    Y(:,i) = Y(:,i-1)+0.1*(-Y(:,i-1).*t1)+delta(i)*U(:,i).*X(:,i-1);
    X(:,i) = X(:,i-1)+0.1*(Z(:,i-1).*trec) - delta(i)*U(:,i).*X(:,i-1);
    Z(:,i) = Z(:,i-1)+0.1*(Y(:,i-1).*t1)-Z(:,i-1).*trec;
end;
figure('Name','Y');
x = 0:timestep:(endTime-timestep);
plot(x,Y(1,:));
xlabel('t [ms]');
ylabel('y');
hold on;
figure;
%x = 1:totalSteps;
x = 0:timestep:(endTime-timestep);
subplot(2,2,1);
plot(x,Y(1,:));
title('ie');
hold on;

subplot(2,2,2);
plot(x,Y(2,:));
title('ee');
hold on;

subplot(2,2,3);
plot(x,Y(3,:));
title('ei');
hold on;

subplot(2,2,4);
plot(x,Y(4,:));
title('ei');
% figure;
% %x = 1:totalSteps;
% x = 0:timestep:(endTime-0.1);
% subplot(2,2,1);
% plot(x,X(1,:));
% hold on;
% 
% subplot(2,2,2);
% plot(x,X(2,:));
% hold on;
% 
% subplot(2,2,3);
% plot(x,X(3,:));
% hold on;
% 
% subplot(2,2,4);
% plot(x,X(4,:));
% 
figure('Name','U');
%x = 1:totalSteps;
x = 0:timestep:(endTime-timestep);
subplot(2,2,1);
plot(x,U(1,:));
hold on;

subplot(2,2,2);
plot(x,U(2,:));
hold on;

subplot(2,2,3);
plot(x,U(3,:));
hold on;

subplot(2,2,4);
plot(x,U(4,:))
