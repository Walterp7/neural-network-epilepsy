set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);

% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);

neuron_pos =  load('neurons.csv');
fs_con =  load('FSconnections.csv');
fs_neuron = fs_con(1,1);
FS_coeffs = [ neuron_pos(  neuron_pos(:,1) ==fs_neuron,3), neuron_pos(  neuron_pos(:,1) == fs_neuron, 4), neuron_pos(  neuron_pos(:,1) == fs_neuron, 5)];
fs_con = fs_con(2:size(fs_con,1));

lts_con =  load('LTSconnections.csv');
lts_neuron = lts_con(1,1);
LTS_coeffs = [ neuron_pos(  neuron_pos(:,1) ==lts_neuron,3), neuron_pos(  neuron_pos(:,1) == lts_neuron, 4), neuron_pos(  neuron_pos(:,1) == lts_neuron, 5)];
lts_con = lts_con(2:size(lts_con,1));
FS_connect = [];
LTS_connect = [];

for indx = 1:size(fs_con,1)
    neur = fs_con(indx,1);
    new_entry = [ neuron_pos(  neuron_pos(:,1) == neur,3), neuron_pos(  neuron_pos(:,1) == neur, 4), neuron_pos(  neuron_pos(:,1) == neur, 5)];
    FS_connect = [FS_connect ;new_entry  ];
end;

for indx = 1:size(lts_con,1)
    neur = lts_con(indx,1);
    new_entry = [ neuron_pos(  neuron_pos(:,1) == neur,3), neuron_pos(  neuron_pos(:,1) == neur, 4), neuron_pos(  neuron_pos(:,1) == neur, 5)];
    LTS_connect = [LTS_connect ;new_entry  ];
end;

figure;
%colorcode according to types
scatter3(neuron_pos(neuron_pos(:,6) == 1,3),neuron_pos(neuron_pos(:,6) == 1,4), neuron_pos(neuron_pos(:,6) == 1,5), '.r' )
hold on;
scatter3(neuron_pos(neuron_pos(:,6) == 2,3),neuron_pos(neuron_pos(:,6) == 2,4), neuron_pos(neuron_pos(:,6) == 2,5), '.b' )
hold on;
scatter3(neuron_pos(neuron_pos(:,6) == 3,3),neuron_pos(neuron_pos(:,6) == 3,4), neuron_pos(neuron_pos(:,6) == 3,5), '.g' )
hold on;
scatter3(neuron_pos(neuron_pos(:,6) == 4,3),neuron_pos(neuron_pos(:,6) == 4,4), neuron_pos(neuron_pos(:,6) == 4,5), 'Marker', '.', 'MarkerEdgeColor',[0,0.6,0.2])
hold off;
set(gca,'XTick', [400,800,1200,1600,2000]);
set(gca,'XTickLabel',{'col 1','col 2','col 3','col 4', 'col 5'});
set(gca,'ZTick', [400,600,1200, 1800])
set(gca,'ZTickLabel',{'II/III','IV','V','VI'})
set(gca,'ZDir','reverse')
grid on;

%line plot for sample FS and LTS
figure;
%fs
for indx = 1:size(fs_con,1)
    neur = fs_con(indx,1);
    new_entry = [ neuron_pos(  neuron_pos(:,1) == neur,3), neuron_pos(  neuron_pos(:,1) == neur, 4), neuron_pos(  neuron_pos(:,1) == neur, 5)];
    line = [ FS_coeffs; new_entry];
    plot3(line(:,1),line(:,2),line(:,3),'b', 'LineSmoothing','on', 'LineWidth', 1);
    hold on;
end;
scatter3(FS_connect(:,1),FS_connect(:,2), FS_connect(:,3), '.b' )
hold on;
scatter3(FS_coeffs(:,1),FS_coeffs(:,2), FS_coeffs(:,3), 'b' )
%lts
for indx = 1:size(lts_con,1)
    neur = lts_con(indx,1);
    new_entry = [ neuron_pos(  neuron_pos(:,1) == neur,3), neuron_pos(  neuron_pos(:,1) == neur, 4), neuron_pos(  neuron_pos(:,1) == neur, 5)];
    line = [ LTS_coeffs; new_entry];
    plot3(line(:,1),line(:,2),line(:,3),'r', 'LineSmoothing','on', 'LineWidth', 1 );
    hold on;
end;
scatter3(LTS_connect(:,1),LTS_connect(:,2), LTS_connect(:,3), '.r' )
hold on;
scatter3(LTS_coeffs(:,1),LTS_coeffs(:,2), LTS_coeffs(:,3), 'r' )
hold off;
set(gca,'XTick', [400,800,1200,1600,2000]);
set(gca,'XTickLabel',{'col 1','col 2','col 3','col 4', 'col 5'});
set(gca,'ZTick', [400,600,1200, 1800])
set(gca,'ZTickLabel',{'II/III','IV','V','VI'})
set(gca,'ZDir','reverse')
grid on;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%scatter plots for sample FS and LTS
figure;
scatter3(FS_connect(:,1),FS_connect(:,2), FS_connect(:,3), '.b' )
hold on;
scatter3(LTS_connect(:,1),LTS_connect(:,2), LTS_connect(:,3), '.r' )
hold on;
scatter3(FS_coeffs(:,1),FS_coeffs(:,2), FS_coeffs(:,3), 'b' )
hold on;
scatter3(LTS_coeffs(:,1),LTS_coeffs(:,2), LTS_coeffs(:,3), 'r' )
xlabel('X (width)')
ylabel('Y (depth)')
zlabel('Z (hight)')
set(gca,'ZDir','reverse')
hold off;