import React from 'react';
import { Link} from 'react-router-dom';
import Dropzone from 'react-dropzone'
import   { post } from 'axios';
class Menu extends React.Component {
	
	constructor() {
		super()
		this.state = { files: [] }
		this.i = 0;
	  }
	  send(file){
		const url = 'http://localhost:3001/sendfile';
		const formData = new FormData();
		formData.append('file',file)
		const config = {
			headers: {
				'content-type': 'multipart/form-data'
			}
		}
		return  post(url, formData,config)
	  }
	  onDrop(file) {
		this.send(file).then((res)=>{
			console.log(res.data)
		})
		  file.id=this.i;
		  this.i++;
		let files= [...this.state.files,file]
		this.setState({
		  files:files
		})

		console.log(this.state)
		
		
	  }
	  onCancel() {
		this.setState({
		  files: []
		});
	  }
	render() {
		
	
		return (
			<div>
			<div className="dropzone">
          <Dropzone
		  	accept=".csv,.xlsx"
            onDrop={this.onDrop.bind(this)}
            onFileDialogCancel={this.
			onCancel.bind(this)}
          >
            <p>Try dropping some files here, or click to select files to upload.</p>
          </Dropzone>
        </div>
        <aside>
          <h2>Dropped files</h2>
          <ul>
            {
              this.state.files.map(f => <li key={f.id}>{f[0].name} - {f[0].size} bytes</li>)
            }
			
          </ul>
        </aside>

				<ul>
					<li>
						<Link to="demo5">Demo 5</Link> - Drag and drop
					</li>
					<li>
						<Link to="demo6">Demo 6</Link> - Serialise & Deserialise
					</li>
					<li>
						<Link to="demo7">Demo 7</Link> - Snap to grid
					</li>
				</ul>
			</div>
		);
	}
}

export default Menu;
