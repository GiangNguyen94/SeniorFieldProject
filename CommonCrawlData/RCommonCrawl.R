require(ggplot2)

df012015=read.csv("CommonCrawlData/data2-technologiesmerged/012015/mergedTechnologies.csv")
df022015=read.csv("CommonCrawlData/data2-technologiesmerged/022015/mergedTechnologies.csv")
df022016=read.csv("CommonCrawlData/data2-technologiesmerged/022016/mergedTechnologies.csv")
df032015=read.csv("CommonCrawlData/data2-technologiesmerged/032015/mergedTechnologies.csv")
df042014=read.csv("CommonCrawlData/data2-technologiesmerged/042014/mergedTechnologies.csv")
df042015=read.csv("CommonCrawlData/data2-technologiesmerged/042015/mergedTechnologies.csv")
df052015=read.csv("CommonCrawlData/data2-technologiesmerged/052015/mergedTechnologies.csv")
df062015=read.csv("CommonCrawlData/data2-technologiesmerged/062015/mergedTechnologies.csv")
df072014=read.csv("CommonCrawlData/data2-technologiesmerged/072014/mergedTechnologies.csv")
df072015=read.csv("CommonCrawlData/data2-technologiesmerged/072015/mergedTechnologies.csv")
df082014=read.csv("CommonCrawlData/data2-technologiesmerged/082014/mergedTechnologies.csv")
df082015=read.csv("CommonCrawlData/data2-technologiesmerged/082015/mergedTechnologies.csv")
df092014=read.csv("CommonCrawlData/data2-technologiesmerged/092014/mergedTechnologies.csv")
df092015=read.csv("CommonCrawlData/data2-technologiesmerged/092015/mergedTechnologies.csv")
df102014=read.csv("CommonCrawlData/data2-technologiesmerged/102014/mergedTechnologies.csv")
df112014=read.csv("CommonCrawlData/data2-technologiesmerged/112014/mergedTechnologies.csv")
df112015=read.csv("CommonCrawlData/data2-technologiesmerged/112015/mergedTechnologies.csv")
df122014=read.csv("CommonCrawlData/data2-technologiesmerged/122014/mergedTechnologies.csv")

df012015=df012015[-c(5),]
df022015=df022015[-c(5),]
df022016=df022016[-c(5),]
df032015=df032015[-c(5),]
df042014=df042014[-c(5),]
df042015=df042015[-c(5),]
df052015=df052015[-c(5),]
df062015=df062015[-c(5),]
df072014=df072014[-c(4),]
df072015=df072015[-c(5),]
df082014=df082014[-c(5),]
df082015=df082015[-c(5),]
df092014=df092014[-c(5),]
df092015=df092015[-c(5),]
df102014=df102014[-c(5),]
df112014=df112014[-c(5),]
df112015=df112015[-c(5),]
df122014=df122014[-c(4),]

pdf("012015.pdf",width=12,height=9)
p1=ggplot(data=df012015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p1)
dev.off()

pdf("022015.pdf",width=12,height=9)
p2=ggplot(data=df022015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p2)
dev.off()

pdf("022016.pdf",width=12,height=9)
p3=ggplot(data=df022016,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p3)
dev.off()

pdf("032015.pdf",width=12,height=9)
p4=ggplot(data=df032015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p4)
dev.off()

pdf("042014.pdf",width=12,height=9)
p5=ggplot(data=df042014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p5)
dev.off()

pdf("042015.pdf",width=12,height=9)
p6=ggplot(data=df042015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p6)
dev.off()

pdf("052015.pdf",width=12,height=9)
p7=ggplot(data=df052015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p7)
dev.off()

pdf("062015.pdf",width=12,height=9)
p8=ggplot(data=df062015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p8)
dev.off()

pdf("072014.pdf",width=12,height=9)
p9=ggplot(data=df072014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p9)
dev.off()

pdf("072015.pdf",width=12,height=9)
p10=ggplot(data=df072015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p10)
dev.off()

pdf("082014.pdf",width=12,height=9)
p11=ggplot(data=df082014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p11)
dev.off()

pdf("082015.pdf",width=12,height=9)
p12=ggplot(data=df082015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p12)
dev.off()

pdf("092014.pdf",width=12,height=9)
p13=ggplot(data=df092014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p13)
dev.off()

pdf("092015.pdf",width=12,height=9)
p14=ggplot(data=df092015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p14)
dev.off()

pdf("102014.pdf",width=12,height=9)
p15=ggplot(data=df102014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p15)
dev.off()

pdf("112014.pdf",width=12,height=9)
p16=ggplot(data=df112014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p16)
dev.off()

pdf("112015.pdf",width=12,height=9)
p17=ggplot(data=df112015,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p17)
dev.off()

pdf("122014.pdf",width=12,height=9)
p18=ggplot(data=df122014,aes(x=technologies,y=number))+geom_bar(stat="identity",color="black",fill="#0000ff")
print(p18)
dev.off()


