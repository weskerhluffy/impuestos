// XXX: https://javascript.info/onload-ondomcontentloaded
function ready() {
	console.log('DOM is ready');
	Array.from(document.getElementsByClassName("deducibilidad_factura_global")).forEach((elem)=>{
		elem.addEventListener("change",(event)=>{
			console.log("Evento para "+event.target.id);
			let checado=event.target.checked;
			console.log("checado "+checado);
			let cuerpoTabla=event.target.parentNode.parentNode.parentNode.parentNode;
			console.log("cuerpo table "+cuerpoTabla.id);
			Array.from(cuerpoTabla.querySelectorAll(".deducibilidad")).forEach((elem1)=>{
				elem1.checked=checado;
			});
		});
	});
}

document.addEventListener("DOMContentLoaded", ready);