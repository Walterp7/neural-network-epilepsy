lfp_data2 = load(['lfp_blockade_blockade50.csv']);
fc = 150; % Cut-off frequency (Hz)
fs = 10000; % Sampling rate (Hz)
Fs = 1000;
tmax = 1;
Nsamps = tmax*Fs;

[b,a] = butter(8, 2*fc/fs,'low')
%b =1;
%a = [1,-1];

filtered = filter(b,a,lfp_data2(1:3000,3));


min_time = 100;
max_time =300;
start_index = find (lfp_data2(:,1)>=min_time,1, 'first');
end_index = find (lfp_data2(:,1)<= max_time,1, 'last');
 lfp1 = lfp_data2;  
 lfp2 = filtered;   


figure;
plot(lfp1(100:3000,1), lfp2(100:3000), 'Color', [0.5,0,1], 'LineWidth', 1,  'LineSmoothing','on')
hold on;
plot(lfp1(100:3000,1), lfp1(100:3000,3), 'Color', 'b', 'LineWidth', 1,  'LineSmoothing','on')
hold off;
legend(['filtered', int2str(fc), '/', int2str(2*fc/fs*500)], 'original');
figure;
order = 5; % Filter order
[B,A] = butter(order,2*fc/fs); % [0:pi] maps to [0:1] here
[sos,g] = tf2sos(B,A)
% sos =
%  1.00000  2.00080   1.00080  1.00000  -0.92223  0.28087
%  1.00000  1.99791   0.99791  1.00000  -1.18573  0.64684
%  1.00000  1.00129  -0.00000  1.00000  -0.42504  0.00000
% 
% g = 0.0029714
%
% Compute and display the amplitude response
Bs = sos(:,1:3); % Section numerator polynomials
As = sos(:,4:6); % Section denominator polynomials
[nsec,temp] = size(sos);
nsamps = 256; % Number of impulse-response samples
% Note use of input scale-factor g here:
x = g*[1,zeros(1,nsamps-1)]; % SCALED impulse signal
for i=1:nsec
  x = filter(Bs(i,:),As(i,:),x); % Series sections
end
%
plot(x); % Plot impulse response to make sure 
          % it has decayed to zero (numerically)
%
% Plot amplitude response 
% (in Octave - Matlab slightly different):
figure;
X=fft(x); % sampled frequency response
f = [0:nsamps-1]*fs/nsamps; grid('on');
axis([0 fs/2 -100 5]); legend('off');
plot(f(1:nsamps/2),20*log10(X(1:nsamps/2)));