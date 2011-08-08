
endTime=2500;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:250:20000)=1;
 delta(3:250:20020)=1;
% delta(16000:1000:24000) = 1;
sf = zeros(3, totalSteps); %u,x,y?

ti =3.0;
trec = 20%20.0;
tfac = 300%900;%1000;
UU = 0.01%0.02;


sf(:,1)=[0;1; 0];
lastSpike = 1;
for i=2:totalSteps
    if(delta(i) == 1)
        dt = abs(lastSpike - i)*timestep;
   
        P = Pmatr( dt, tfac, trec, ti);
        sf(:,i) = P* [sf(:,lastSpike);1];
        s0=[UU*(1- sf(1,i)); -sf(2,i)*(sf(1,i)+UU*(1-sf(1,i)));sf(2,i)*(sf(1,i)+UU*(1-sf(1,i))) ];
        sf(:,i) = sf(:,i)+s0;
        lastSpike = i;
    end;
   
end


resultRS2LTS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultRS2LTS = resultRS2LTS + sf(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;
normParam = max(resultRS2LTS(1,1:50))
resultRS2LTS = resultRS2LTS/normParam;
%---------------------like in Java -----------------------------
u = zeros(1, totalSteps);
y =  zeros(1, totalSteps);
x =  zeros(1, totalSteps);
x(1,1)=1;
lastSpike = 1;

for i=2:1:totalSteps
    if(delta(1,i)>0)
    dStep = i - lastSpike;
    dt = dStep*0.1;
    u(1, i) = u(1,i-dStep) * exp(-dt / tfac) * (1 - UU) + UU;
    xtmp = (x(1,i-dStep) * exp(-dt / trec) + y(1,i-dStep) * ti * (exp(-dt /trec) - exp(-dt / ti))/ (ti - trec)+ 1 - exp(-dt / trec));

    x(1,i) = xtmp * (1 - u(1,i));
    y(1,i) = (y(1,i-dStep)*exp(-dt / ti) + xtmp * u(1,i));
    lastSpike=i;
    end;
end;



%-----------------PLOT---------------------------------------
figure('Name','Facilitation');
subplot(4,2,5:8);
%plot(timeEPSP, resultRS2LTS);
plot(timeEPSP, y(1,:));

hold on;
subplot(4,2,1:4);
%plot(timeEPSP, resultRS2LTS);
plot(timeEPSP, sf(3,:));
title('EPSP, RS to LTS');
xlabel('time [ms]');
%ylim([-0.1,9]);