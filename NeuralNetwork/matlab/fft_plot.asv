function [ freq, fft_result ] = fft( filename, color )
%function plots abs of fft coefficients
% arguments: filenam - a string with the name of the file (path)
% color - char for the color ('b','r','g' etc)

%read the file
eeg_gamma = load(filename);

time = transpose(eeg_gamma(2000:length(eeg_gamma),1));
eeg = transpose(eeg_gamma(2000:length(eeg_gamma),2));
%figure;
length(eeg);
%plot(time, eeg, 'r');
%hold on;


%[b,a]= butter(2,10/5000,'high');
%filt_eeg = filter(b,a,eeg);
%plot(time,filt_eeg)

%eeg = filt_eeg;
%figure;
%plot(time, eeg);
%title('EEG over time');


Fs = length(eeg);
T = 1/Fs;
L=length(eeg);
%NFFT = 2^nextpow2(L);
NFFT = L;
Y = fft(eeg,NFFT)/L;
f = Fs/2*linspace(0,1,NFFT/2+1);

% figure;
% plot(f,2*abs(Y(1:NFFT/2+1)))
% title('Absolute value of the FFT coefficients');
% 
 result = 2*abs(Y(1:NFFT/2+1));
% 
% toSave = [transpose(f),transpose(result)];
% csvwrite('result_fft.csv', toSave);
% 
% %open the file - has to already EXIST 
% 
% figure;
% plot(f,result)
% figure;
% plot(f(20:200),result(20:200), color)
% xlabel('[Hz]');
% ylabel('amplitude');
% title(strcat('FFT - simulation ', filename));
% 
% figure;
% plot(f,result)
% figure;
% plot(f(10:200),result(10:200), color)
% xlabel('[Hz]');
% ylabel('amplitude');
% title(strcat('FFT - simulation ', filename));
fft_result = result;
freq = f;
%figure;
%plot(ifft(Y));
end

