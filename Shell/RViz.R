require(plotly)
args = commandArgs(trailingOnly = TRUE)
filename = args[1]
type = args[2]
graphname=paste0(type, "_trend_for_repo_", filename)
df = read.csv(filename,1)
df$time=as.POSIXct(df$time)
mo = strftime(df$time,"%y/%m")
dd = data.frame(mo,df$time)
dd$df.repo_time=1
dd.agg <- aggregate(dd$df.repo_time ~ mo, dd, FUN = sum)
dd.agg$group=1
colnames(dd.agg)=c("Month","Times","Group")
graphname=sub("csv","pdf",graphname)
pdf(graphname,width=12,height=9)
p = ggplot(data=dd.agg,aes(x=Month,y=Times,group=Group))+ggtitle(graphname)+geom_line()+geom_point()
print(p + theme(axis.text.x=element_text(angle=90)))
dev.off()