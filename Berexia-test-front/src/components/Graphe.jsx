import React from 'react';
import tools from '..'
import {
	DiagramWidget,
	DefaultNodeModel,
	DefaultPortModel,
} from 'storm-react-diagrams';
import TrayWidget from './TrayWidget';
import TrayItemWidget from './TrayItemWidget';
import  axios from 'axios';
import '../srd.css';
class Graphe extends React.Component {
	state={
		listNode:[]
	}
	componentWillMount() {
		this.engine = tools.engine
		this.tempe=''
		this.filesList=''
		this.model = tools.model
		this.form=''
		this.selected
		this.finalNodes
		this.requests=[]
		this.value=''
		this.schema=''
		this.schema2=''
		this.inFile
		this.inFile2
		}
	showNodes = async()=>{
		let nodes = this.engine.getDiagramModel().nodes;
		let links = this.engine.getDiagramModel().links;
		let i=0
		let In
		let out
		let listNode=[]
		for(var prop in nodes){	
		let node={
			id:"",
			name:"",
			req:"",
			inNode:[],
			outNode:[],
		}
		let ports = nodes[Object.keys(nodes)[i]].ports;
		if([Object.keys(ports)[0]][0]==="out-1"){
			out = ports[Object.keys(ports)[0]].id;
		}else{
			In = ports[Object.keys(ports)[0]].id;
			out = ports[Object.keys(ports)[1]].id;
			console.log(out)
		}
		 node.id= prop;
		 node.name= nodes[Object.keys(nodes)[i]].name
		let j=0
		let k=0
		let l=0
		for(var pro in links){
		let link = links[Object.keys(links)[j]];
			
			if(link.targetPort.in!==true){
				link.remove();
			}else{
				console.log()
				if (link.targetPort.id===In){
					node.inNode[k]=link.sourcePort.getParent().id
					k++;
				}else{
					if (link.sourcePort.id===out){
						console.log(out)
						node.outNode[l]=link.sourcePort.getParent().id
						l++;
						}
					}
				j++;
			}
		}
	listNode[i]=node;
	i++;
	out=""
	In=""
	}
	this.listNode=listNode
	this.setState({listNode},()=>{
	console.log(this.state)
	});
}
confirmSelection = (e)=>{
	this.requests[this.selected] = this.requests[this.selected]+' from ?'
	this.schema = ''
}
send= ()=>{
	console.log(this.requests)
	let i=0
	for(i=0;i<this.listNode.length;i++){
		if(this.requests[i]!==undefined){
		this.listNode[i].req=this.requests[i]
		}
	}
	console.log(this.listNode)
	axios.defaults.headers = {
		'Content-Type': 'application/json'
	}
	  axios.post('http://localhost:8080/api/execute',this.listNode).then(data=>{
	console.log(data)  
  })
}
add = (e)=>{
	let value =e.target.getAttribute('name')
	this.requests[this.selected]=this.requests[this.selected]+' '+value 
}
handleInputChange = (e)=>{
	const target = e.target;
	const name = target.name;
	if(this.requests[this.selected]!==undefined){
	this.requests[this.selected]=this.requests[this.selected]+','+name
	}else{
		if(this.listNode[this.selected].name=='select'){
		this.requests[this.selected]='select '+name
	}else{
		this.requests[this.selected]=''+name
	}
	}
}
temp =(e)=>{
	this.tempe= e.target.value
}
onSubmitInput=(e)=>{
	e.preventDefault()
	this.requests[this.selected]=this.requests[this.selected]+' '+this.tempe
}
render() {
		axios.get('http://localhost:8080/api/getAllFiles').then((data)=>{
			this.files=data.data
			console.log(this.files)
         this.filesList =  Object.keys(this.files).map( file =>{
            return(
                <TrayItemWidget model={{ type: 'file',color:'green',name:file }} key={file} name={file} />
            )
		})
	})
		return (
			<div className="content">
			<div className="row" >
			<div className="col-2">
				<button onClick={this.confirmSelection} className="btn-secondary" >confirm Selection</button>
				{this.schema}
				</div>
				<div className="col-8" >
				<div
					className="diagram-layer"
					onDoubleClick={
						event=>{
							let i=0;
							this.showNodes().then(()=>{
							console.log(event._targetInst)
							let div = document.getElementsByClassName('node selected')
							let id=div[0].getAttribute('data-nodeid')
							for (i=0;i<this.state.listNode.length;i++){
								console.log(this.state.listNode[i].id)
								if(this.state.listNode[i].id===id){
									this.selected=i	
								}
							}if(this.listNode[this.selected].name==='select'){
								let j=0
									for(j=0;j<this.listNode.length;j++){
									if(this.listNode[j].id===this.listNode[this.selected].inNode[0]){
										this.inFile=this.listNode[j]
									}
								}
								console.log(this.inFile)
								console.log(this.files)
							this.schema=this.files[this.inFile.name].map(att=>{
								return(
									<div><input type="checkbox" name={att} className="custom-control custom-checkbox" key={att} value={att} onChange={this.handleInputChange} ></input> {att}</div>
								)
								
							})
							console.log(this.schema)
							let d='<'
							this.form=(
								<div>
									<button className="btn-secondary" name="where" onClick={this.add}>where</button>
									<button name=">" onClick={this.add}  >></button>
									<button name="<" onClick={this.add}  >{d}</button>
									<button name="=" onClick={this.add} >=</button>
									<button  name="like" onClick={this.add} >like</button>
									<form onSubmit={this.onSubmitInput}>
										<input type="text" onChange={this.temp} ></input>
									</form>
								</div>
							)		
						}	else{
							this.schema=''
							this.form=''
							if(this.listNode[this.selected].name==='combine'){
								let j=0
									for(j=0;j<this.listNode.length;j++){
									if(this.listNode[j].id===this.listNode[this.selected].inNode[0]){
										this.inFile=this.listNode[j]
										
									}else{
										if(this.listNode[j].id===this.listNode[this.selected].inNode[1]){
										this.inFile2=this.listNode[j]
										
									}
									}
							}
							this.schema=this.files[this.inFile.name].map(att=>{
								return(
									<div><input type="checkbox" name={att} className="custom-control custom-checkbox" key={att} value={att} onChange={this.handleInputChange} ></input> {att}</div>
								)
							})
							this.schema2=this.files[this.inFile.name].map(att=>{
								return(
									<div><input type="checkbox" name={att} className="custom-control custom-checkbox" key={att} value={att} onChange={this.handleInputChange} ></input> {att}</div>
								)
							})
							}
						}												
						})
						}
					}
					
					onDrop={event => {
						var data = JSON.parse(event.dataTransfer.getData('storm-diagram-node'));
						console.log(data)
						var node = null;
						if (data.type !== 'file') {
							node = new DefaultNodeModel(data.name, data.color);
							node.addPort(new DefaultPortModel(true, 'in-1', 'In'));
							node.addPort(new DefaultPortModel(false, 'out-1', 'Out'));
						} else {
							node = new DefaultNodeModel(data.name,data.color);
							node.addPort(new DefaultPortModel(false, 'out-1', 'Out'));	
						}
						var points = this.engine.getRelativeMousePoint(event);
						node.x = points.x;
						node.y = points.y;
						this.engine.getDiagramModel().addNode(node);
						this.forceUpdate();
					}}
					onDragOver={event => {
						event.preventDefault();

					}}
				>	
					<DiagramWidget diagramEngine={this.engine} />
				</div>
				</div>
				<div className="col-2">
				<TrayWidget>
					<TrayItemWidget model={{ type: 'select', color:'peru', name:'select' }} name="select"  />
					<TrayItemWidget className='btn btn-primary' model={{ type: 'groupBy', color:'hotpink',name:'group by' }} name="groupBy"/>
					<TrayItemWidget model={{ type: 'combine',color:'#b00',name:'combine' }} name="combine" />
					{this.filesList}
				</TrayWidget>
				<button className="btn-secondary" onClick={this.send} >send</button>
				{this.form}
				{this.schema2}
					</div>
				</div>
			</div>
		);
	}
}
export default Graphe;




